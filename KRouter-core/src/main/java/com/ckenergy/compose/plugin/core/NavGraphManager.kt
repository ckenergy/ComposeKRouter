package com.ckenergy.compose.plugin.core

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import java.net.URLEncoder

/**
 * @author ckenergy
 * @date 2023/2/15
 * @desc compose配置的路由管理，不同的module可以通过实现[composeModules]方法往里面配置需要的页面路由，
 * 然后在 [Application.onCreate]添加[composeModules] 方法往里面添加各模块实现了[composeModules]接口的实例
 */
object NavGraphManager {

    private val graphContainer = hashSetOf<ModuleBuilder>()

    private val routeMap = hashMapOf<String, List<NavGraphDestination>>()

    var pluginLoader: IPluginLoader? = null

    var notFindPage: (@Composable (String) -> Unit)? = null

    var isAnimation = true

    private lateinit var builder: NavGraphBuilder

    init {
        addModules(baseNavGraph)
        registerMap()
    }

    private fun registerMap() {

    }

    /**
     * 添加模块
     */
    fun addModules(compose: ModuleBuilder) {
        graphContainer.add(compose)
    }

    /**
     * 加载配置好的路由模块
     */
    @OptIn(ExperimentalAnimationApi::class)
    fun initKRouter(
        builder: NavGraphBuilder,
        controller: NavController,
        action: NavGraphManager.() -> Unit
    ) {
        this.builder = builder
        action(NavGraphManager)
        graphContainer.forEach {
            buildNavGraph(controller, it) { it1 ->
                NavGraphManager.builder.run {
                    if (isAnimation) {
                        animationCompose(it1)
                    } else {
                        composable(it1.route, it1.arguments, content = { it2 ->
                            it1.content(it2)
                        })
                    }
                }
            }
        }
    }

    /**
     * 加载配置好的路由模块
     */
    @OptIn(ExperimentalAnimationApi::class)
    fun composablePlugIn(controller: NavController, compose: ModuleBuilder) {
        buildNavGraph(controller, compose) {
            builder.composablePlugin(controller.graph, it.route, it.arguments, content = { it1 ->
                it.content(it1)
            })
        }
    }

    private fun buildNavGraph(
        controller: NavController,
        compose: ModuleBuilder,
        action: (NavGraphDestination) -> Unit
    ) {
        val container = NavGraphContainer()
        compose(container, controller)
        routeMap[container.packageName] = container.list
        container.list.forEach { it1 ->
            action(it1)
        }
    }

    fun findPackageName(route: String): String? {
        routeMap.forEach {
            val match = it.value.find { it1 ->
                it1.route == route
            }
            if (match != null)
                return it.key
        }
        return null
    }

    fun navigate(controller: NavController, route: String) {
        val request =
            NavDeepLinkRequest.Builder.fromUri(NavDestination.createRoute(route).toUri()).build()
        val match = controller.graph.matchDeepLink(request)
        if (match == null) {//动态加载
            pluginLoader?.load(route, controller, builder)
        }
        val match1 = controller.graph.matchDeepLink(request)
        if (match1 == null) {
            controller.navigate(NOT_FIND_ROUTE + URLEncoder.encode(route, "utf-8"))
        } else {
            controller.navigate(route)
        }
    }

}

fun NavController.navigateRoute(route: String, action: HashMap<String, Any>.() -> Unit = {}) {
    val map = hashMapOf<String, Any>()
    action(map)
    val newRoute = if (map.isNotEmpty()) {
        DestinationProvider.getDestination(route, map)
    } else route
    NavGraphManager.navigate(this, newRoute)
}

fun composeModules(action: ModuleBuilder) = action

@ExperimentalAnimationApi
internal fun NavGraphBuilder.composablePlugin(
    graph: NavGraph,
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    val match = graph.matchDeepLink(
        NavDeepLinkRequest.Builder.fromUri(
            NavDestination.createRoute(route).toUri()
        ).build()
    )
    if (match == null) {
        val destination = if (NavGraphManager.isAnimation)
            AnimatedComposeNavigator.Destination(
                provider[AnimatedComposeNavigator::class],

                ) {
                content(it)
            }
        else
            ComposeNavigator.Destination(provider[ComposeNavigator::class], content)

        destination.apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    }
}