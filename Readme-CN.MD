## ComposeKRouter组件化路由框架

### 使用方式 (last_version = "1.0.1")

#### 1、添加依赖
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
使用到@KRouter注解的module build.gradle
```groovy
ksp {
    arg("KROUTER_PACKAGE", "you module namespace")
}
ksp("io.github.ckenergy:kRouter-compiler:$KRouter_version")
```


#### 2、给一个页面加上@KRouter注册路由（就无需再NavHost给每个页面注册路由了）
``` kotlin
@KRouter(ComposeRouterMapper.Second)
@Composable
fun SecondPage(int: Int, string: String? = "default", testBean: TestBean, vararg long: Long)
注意：函数上的参数只能放简单的数据（基本类型和bean之类的，因为这块用的是Gson来序列化的）

上面的代码等于原始注册路由的方式

NavHost() {
    composable(ComposeRouterMapper.Second) {
        //... 获取参数
        SecondPage(....)
    }
}

```
#### 3、在NavHost初始化
``` kotlin
NavHost() {
    NavGraphManager.initKRouter(context, this, navController) {
        notFindPage = { route: String -> NotFindPage(route) }
    }
}
```
#### 4、跳转的时候带上参数就可以啦
``` kotlin
controller.navigateRoute(ComposeRouterMapper.Second) {
    put("int", 1)//key的名字要和SecondPage函数参数名要保持一致一一对应
    put("long", longArrayOf(1L, 3L))
    put("testBean", TestBean(1.5f, false))
}
```
