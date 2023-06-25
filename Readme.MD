## Compose插件化

之前有同事分享过shadow这个插件化框架，当时对这块还挺感兴趣的，但是因为有一些其他的问题一直没有深入，比如singleTask的Activity要先定义好多个占位，后面学习了compose，非常喜欢用compose构建界面，看到compose是单Activity的模式，看了一下页面注册路由的原理，发现是可以动态加载路由的，所以和大家分享下

#### 1、Navigator简单介绍

首先需要简单的熟悉下这里路由的跳转逻辑及分工

**NavHost** 定义路由和页面的地方

**NavController** 导航的全局管理者，可以用它来进行页面跳转，维护着NavGraph路由索引图和回退栈NavBackStacks

**NavGraph** 定义导航时，需要收集各个节点的导航信息，并统一注册到导航图中

**NavDestination** 导航中的各个节点，携带了 route，arguments 等信息

**Navigator** 导航的具体执行者，NavController 基于导航图获取目标节点，并通过 Navigator 执行跳转

#### 2、Navigator注册原理

其实compose也是适配了Jetpack Navigatioin 导航框架，在他的基础上加了一些自己的东西，比如NavHost是注册页面路由的地方，相当于Activity在Manifest注册的情况

大概注册方式如下
``` kotlin
NavHost(navController = navController,
    startDestination = startDestination,) {
    composable("route") {
        MainPage()
    }
}
```
然后我们看看composable这个方法，源码里面做了些什么事，首先会把route和arguments包装成一个NavDestination，在添加到NavGraphBuilder的list里面，再build的时候再添加到NavGraph里面
``` kotlin
public fun NavGraphBuilder.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class], content).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}

override fun build(): NavGraph = super.build().also { navGraph ->
        navGraph.addDestinations(destinations)
        ...
    }
```
所以我们想要动态添加路由，只需要获取NavGraph然后添加就行了，NavGraph又能从NavHostController中获取

#### 3、动态加载路由

现在我们来验证一下，模仿composable写一个加载路由的方法composablePlugin
``` kotlin 
@ExperimentalAnimationApi
fun NavGraphBuilder.composablePlugin(
    graph: NavGraph,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    val match = graph.matchDeepLink(NavDeepLinkRequest.Builder.fromUri(NavDestination.createRoute(route).toUri()).build())
    if (match == null)
        graph.addDestination(
            AnimatedComposeNavigator.Destination(
                provider[AnimatedComposeNavigator::class],
                content
            ).apply {
                this.route = route
                arguments.forEach { (argumentName, argument) ->
                    addArgument(argumentName, argument)
                }
                deepLinks.forEach { deepLink ->
                    addDeepLink(deepLink)
                }
            }
        )
}
```

上面我们已经验证可以动态加载路由了，现在我们该考虑如何动态加载一个插件apk了，然后再读取和配置里面的路由，再跳转到这个模块的页面

首先我们先建立两个App，一个是main一个就叫other，这里我写了一个简单的组件化用来注册路由的框架，具体大家可以看源码里的实现，大概就是每个module都需要建一个NavGraph的文件，然后在里面注册这个module所有页面的路由，大概如下
``` kotlin
val navGraph = composeModules { controller ->
    //插件包名
    packageName = "com.ckenergy.compose.other"
    composable(ComposeRouterMapper.Other.url) {
        OtherPage {
            controller.navigate1(ComposeRouterMapper.Second.url)
        }
    }
}
```
还要写一个动态加载apk的方法
``` kotlin
    fun loadApk(ctx: Context?, apkPath: String, packageName: String) {
        if (ctx == null) {
            Log.d(TAG, "ctx is null, apk cannot be loaded dynamically")
            throw RuntimeException("ctx is null, apk cannot be loaded dynamically")
        }
        try {
            val appCtx = ctx.applicationContext
            val pluginDexClassLoader = DexClassLoader(
                apkPath,
                appCtx!!.getDir("dex2opt", Context.MODE_PRIVATE).absolutePath,
                null,
                appCtx!!.classLoader
            )
            val pluginPackageArchiveInfo =
                appCtx!!.packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)!!

            val pluginAssets = AssetManager::class.java.newInstance()
            val addAssetPath: Method =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(pluginAssets, apkPath)
            val superResources: Resources? = ctx.resources
            val pluginRes = Resources(
                pluginAssets,
                superResources?.displayMetrics,
                superResources?.configuration
            )
            pluginInfoMap[packageName] = PluginInfo(packageName, appCtx, pluginAssets, pluginRes, pluginDexClassLoader, pluginPackageArchiveInfo)
            Log.d(TAG, "dynamic loading of apk success")
        } catch (e: Exception) {
            Log.d(TAG, "dynamic loading of apk failed")
            e.printStackTrace()
        }
    }
```
然后再查找apk里面的路由再加载
``` kotlin
private fun addRoute(context: Context, controller: NavController) {
    try {
        val cls =
            PluginManager.getPluginInfo(Constants.OTHER_PKG)?.pluginDexClassLoader!!.loadClass(
                Constants.OTHER_PKG+".NavGraphKt"
            )
        val graph = cls.declaredMethods.first().invoke(null) as ModuleBuilder
        NavGraphManager.composablePlugIn(controller, graph)
        Toast.makeText(context, "add route success", Toast.LENGTH_SHORT).show()
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: InstantiationException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
}
```
加载完成后调用controller.navigate就可以直接跳转了

这里我们简单写两个按钮，一个加载路由，另外一个跳转，具体可以看源码，最后测试是可以跳转成功的

但是有一个资源获取的问题，插件里面getResource获取的是宿主的，这里我们可能要通过字节码插桩去hook compose包androidx.compose.ui.res.Resources的这个方法resources()，然后返回插件的resource

[github地址](https://github.com/ckenergy/ComposePlugIn)

#### 4、todo
1、hook resource()方法
2、用ksp生成composeModules简化框架

最近工作太忙，本来两个月前就写好了这块代码了，想完善好再分享出来的，但是一直没有时间，所以先把自己的发现分享出来，有兴趣的可以完善一下，如果对上面的两个ToDo有想法的欢迎和我交流（2ckenergy@gmail。com），避免大家重复造轮子：）