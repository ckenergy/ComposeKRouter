package com.ckenergy.compose.plugin.core

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.core.net.toUri
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import com.google.accompanist.navigation.animation.composable
import java.net.URLEncoder


//公用一个NavHostController 全局获取当前可跳转
@SuppressLint("CompositionLocalNaming")
val AppNavController = compositionLocalOf<NavHostController> { error("NavHostController error") }

/**
 * @author ckenergy
 * @date 2023/2/15
 * @desc compose配置的路由管理，不同的module可以通过实现[composeModules]方法往里面配置需要的页面路由，
 * 然后在 [Application.onCreate]添加[composeModules] 方法往里面添加各模块实现了[composeModules]接口的实例
 */
object NavGraphManager {

    private val graphContainer = hashSetOf<ModuleBuilder>()

    var pluginLoader: IPluginLoader? = null

    var notFindPage: (@Composable (String) -> Unit)? = null

    private lateinit var builder: NavGraphBuilder

    init {
        addModules(baseNavGraph)
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
    fun composable(builder: NavGraphBuilder, controller: NavController) {
        this.builder = builder
        graphContainer.forEach {
            val container = NavGraphContainer()
            it(container, controller)
            container.list.forEach { it1 ->
                builder.composableHorizontal(it1.route, it1.arguments, content = it1.content)
            }
        }
    }

    /**
     * 加载配置好的路由模块
     */
    @OptIn(ExperimentalAnimationApi::class)
    fun composablePlugIn(controller: NavController, compose: ModuleBuilder) {
        val container = NavGraphContainer()
        compose(container, controller)
        container.list.forEach { it1 ->
            builder.composablePlugin(controller.graph, it1.route, it1.arguments, content = it1.content)
        }
    }

    fun navigate(controller: NavController, route: String) {
        val request = NavDeepLinkRequest.Builder.fromUri(NavDestination.createRoute(route).toUri()).build()
        val match = controller.graph.matchDeepLink(request)
        if (match == null) {//动态加载
            pluginLoader?.load(route, controller, builder)
        }
        val match1 = controller.graph.matchDeepLink(request)
        if (match1 == null) {
            controller.navigate(NOT_FIND_ROUTE+URLEncoder.encode(route, "utf-8"))
        }else {
            controller.navigate(route)
        }
    }

}

fun NavController.navigate1(route: String){
    NavGraphManager.navigate(this, route)
}

fun composeNav(action: NavGraphManager.() -> Unit) {
    action(NavGraphManager)
}

fun composeModules(action : ModuleBuilder) = action

/**
 * compose页面跳转平移动画
 */
@ExperimentalAnimationApi
fun NavGraphBuilder.composableHorizontal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        content = content,
    )
}

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

/**
 * compose页面跳转竖直动画
 */
@ExperimentalAnimationApi
fun NavGraphBuilder.composableVertical(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    this@composableVertical.composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            // Let's make for a really long fade in
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Up,
                animationSpec = tween(500)
            )
        },
        content = content,
    )
}