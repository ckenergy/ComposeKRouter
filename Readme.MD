## ComposeKRouter is a component router

[中文文档](Readme-CN.MD)

### how to use (last_version = "1.0.3")

#### 1、add dependencies
root build.gradle
``` groovy
dependencies {
    classpath  "io.github.ckenergy:KRoutePlugin:$KRouter_version"
}
```
app build.gradle
``` groovy
implementation("io.github.ckenergy:kRouter-core:$KRouter_version")
```
add in module build.gradle where use @KRouter
```groovy
ksp {
    arg("KROUTER_PACKAGE", "you module namespace")
}
ksp("io.github.ckenergy:kRouter-compiler:$KRouter_version")
```


#### 2、add @KRouter in @Composable function（no need add compose in NavHost）
``` kotlin
@KRouter(ComposeRouterMapper.Second)
@Composable
fun SecondPage(int: Int, string: String? = "default", testBean: TestBean, vararg long: Long)

Note: The parameters on the function can only put simple data (primitives , string and beans, because this piece is serialized by Gson)

The code above is equal to the original way of registering routes

NavHost() {
    composable(ComposeRouterMapper.Second) {
        //... get parameters
        SecondPage(....)
    }
}

```
#### 3、add in NavHost
``` kotlin
NavHost() {
    NavGraphManager.initKRouter(context, this, navController) {
        notFindPage = { route: String -> NotFindPage(route) }
    }
}
```
#### 4、put data when navigate
``` kotlin
controller.navigateRoute(ComposeRouterMapper.Second) {
    put("int", 1)//key的名字要和SecondPage函数参数名要保持一致一一对应
    put("long", longArrayOf(1L, 3L))
    put("testBean", TestBean(1.5f, false))
}
```


#### 5、Custom registration route **composeModules**
When your compose page parameters are more complex, such as parameter is function
``` kotlin
@Composable
fun OtherPage(start: () -> Unit)
```
At this time, you need to customize your own to register the route
``` kotlin
val navGraph = composeModules { controller ->
    packageName = "com.ckenergy.compose.other"
    composable(ComposeRouterMapper.Other) {
        OtherPage {
            controller.navigateRoute(ComposeRouterMapper.Second)
        }
    }
}

NavGraphManager.initKRouter(context, this, navController) {
    notFindPage = { route: String -> NotFindPage(route) }
    isAnimation = true
    isDebug = BuildConfig.DEBUG
    addModules(navGraph)//add route map
}
```
