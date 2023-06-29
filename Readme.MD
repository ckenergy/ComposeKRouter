## ComposeKRouter组件化路由框架

### 使用方式

#### 1、添加依赖
#### 1、给一个页面加上@KRouter注册路由（就无需再NavHost给每个页面注册路由了）
``` kotlin
@KRouter(ComposeRouterMapper.Second)
@Composable
fun SecondPage(int: Int, string: String? = "default", testBean: TestBean, vararg long: Long)

上面的代码等于原始注册路由的方式

NavHost() {
    composable(ComposeRouterMapper.Second) {
        //... 获取参数
        SecondPage(....)
    }
}

```
#### 2、在NavHost初始化
``` kotlin
NavHost() {
    NavGraphManager.initKRouter(this, navController) {
        notFindPage = { route: String -> NotFindPage(route) }
    }
}
```
#### 3、跳转的时候带上参数就可以啦
``` kotlin
controller.navigateRoute(ComposeRouterMapper.Second) {
    put("int", 1)//key的名字要和SecondPage函数参数名要保持一致一一对应
    put("long", longArrayOf(1L, 3L))
    put("testBean", TestBean(1.5f, false))
}
```
