## Compose插件化

之前有同事分享过shadow这个插件化框架，当时对这块还挺感兴趣的，但是因为有一些其他的问题一直没有深入，比如Activity的加载之类的局限，后面学习了compose，非常喜欢用compose构建界面，看到compose是单Activity的模式，看了一下页面注册路由的原理，发现是可以动态加载路由的，所以和大家分享下

首先需要简单的熟悉下这里路由的跳转逻辑及分工

NavHost 定义路由和页面的地方

NavController 导航的全局管理者，可以用它来进行页面跳转，维护着NavGraph路由索引图和回退栈NavBackStacks

NavGraph 定义导航时，需要收集各个节点的导航信息，并统一注册到导航图中

NavDestination 导航中的各个节点，携带了 route，arguments 等信息

Navigator 导航的具体执行者，NavController 基于导航图获取目标节点，并通过 Navigator 执行跳转


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

然后写两个按钮，一个加载路由，另外一个跳转，这里就不贴代码了，最后测试是可以跳转成功的

上面我们已经验证可以动态加载路由了，现在我们该考虑如何动态加载一个apk了，然后再读取和配置里面的路由，然后再调整到这个模块的页面

首先我们先建立两个App，一个是main一个就叫other，这里我写了一个方便组件化用来注册路由的框架，具体大家可以看源码里的实现，大概就是每个module都需要建一个NavGraph的文件，然后在里面注册这个module所有页面的路由，大概如下
``` kotlin
val navGraph = composeModules { controller ->
    packageName = "com.ckenergy.compose.other"
    composable(ComposeRouterMapper.Other.url) {
        OtherPage {
            controller.navigate1(ComposeRouterMapper.Second.url)
        }
    }
}
```



当我们要跳转某个页面的时候我们会调用**navController.navigate**方法

首先我们先看看compose
