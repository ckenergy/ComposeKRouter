package com.ckenergy.compose.krouter.core

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.composable
import com.ckenergy.compose.krouter.core.utils.ClassUtils
import com.ckenergy.compose.krouter.core.utils.Constants
import com.ckenergy.compose.krouter.core.utils.Constants.ROUTER_SP_CACHE_KEY
import com.ckenergy.compose.krouter.core.utils.Constants.ROUTER_SP_KEY_MAP
import com.ckenergy.compose.krouter.core.utils.PackageUtils
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator
import java.net.URLEncoder

private const val TAG = "NavGraphManager"
/**
 * @author ckenergy
 * @date 2023/2/15
 * @desc compose配置的路由管理，不同的module可以通过实现[composeModules]方法往里面配置需要的页面路由，
 * 然后在 [Application.onCreate]添加[composeModules] 方法往里面添加各模块实现了[composeModules]接口的实例
 */
object NavGraphManager {

    private var context: Application? = null

    private val graphContainer = hashSetOf<ModuleBuilder>()

    private val routeMap = hashMapOf<String, List<NavGraphDestination>>()

    var pluginLoader: IPluginLoader? = null

    var notFindPage: (@Composable (String) -> Unit)? = null

    var isAnimation = true

    var isDebug = false

    private var hasRegister = false

    private lateinit var builder: NavGraphBuilder

    init {
        addModules(baseNavGraph)
        registerMap()
    }

    private fun registerMap() {
        hasRegister = false
        // addModules(baseNavGraph) asm inject
        // hasRegister = true
    }

    @JvmStatic
    fun registerDex(context: Application) {
        Log.d(TAG, "registerDex hasRegister:$hasRegister")
        if (hasRegister) {
            return
        }
        synchronized(this) {
            if (hasRegister) {
                return
            }
            val startTime = System.currentTimeMillis()
            try {
                val classSet: List<Class<*>>?
                if (isDebug || PackageUtils.isNewVersion(context)) {
                    classSet = ClassUtils.getFileNameByName(context, Constants.NAVGRAPH_CLASS).map { Class.forName(it) }.filter {
                        INavGraphProvider::class.java.isAssignableFrom(it) && it != INavGraphProvider::class.java
                    }

                    val nameSet = classSet.map { it.canonicalName }.toSet()
                    context.getSharedPreferences(ROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE).edit()
                        .putStringSet(ROUTER_SP_KEY_MAP, nameSet).apply()
                    Log.d(TAG, "find in dex class:$nameSet")
                    PackageUtils.updateVersion(context)
                }else {
                    val nameSet = context.getSharedPreferences(
                        ROUTER_SP_CACHE_KEY,
                        Context.MODE_PRIVATE
                    ).getStringSet(ROUTER_SP_KEY_MAP, HashSet<String>())
                    Log.d(TAG, "load from cache class:$nameSet")
                    classSet = nameSet?.map { Class.forName(it) }
                }

                if (!classSet.isNullOrEmpty())
                    for (clazz in classSet) {
                        clazz.getDeclaredMethod(Constants.REGISTER_NAME, NavGraphManager::class.java).invoke(null, NavGraphManager)
                    }
            }catch (e: Exception) {
                e.printStackTrace()
            }

            Log.d(TAG, "registerDex time:${System.currentTimeMillis() - startTime}")
            hasRegister = true
        }

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
    fun initKRouter(
        context: Context,
        builder: NavGraphBuilder,
        controller: NavController,
        action: NavGraphManager.() -> Unit
    ) {
        val application = context.applicationContext as Application
        this.context = application
        registerDex(application)
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
        graph.addDestination(destination)
    }
}