package com.ckenergy.compose

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import com.ckenergy.base.compose.BuildConfig
//import androidx.navigation.compose.composable
import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.krouter.core.IPluginLoader
import com.ckenergy.compose.krouter.core.NavGraphManager
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

//公用一个NavHostController 全局获取当前可跳转
@SuppressLint("CompositionLocalNaming")
val AppNavController = compositionLocalOf<NavHostController> { error("NavHostController error") }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    context: Context,
    startDestination: String = ComposeRouterMapper.Main,
    finishActivity: (Boolean) -> Unit = {}
) {
    val navController = AppNavController.current
    val route = startDestination.ifBlank { ComposeRouterMapper.Main }

    val springSpec = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )

    AnimatedNavHost(
        navController = navController,
        startDestination = route,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = springSpec)
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = springSpec
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Right,
                animationSpec = springSpec
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentScope.SlideDirection.Left,
                animationSpec = springSpec
            )
        },
    ) {
        NavGraphManager.initKRouter(context, this, navController) {
            pluginLoader = object : IPluginLoader {
                override fun load(route: String, controller: NavController, builder: NavGraphBuilder) {
//                    composablePlugIn(controller, libNavGraph)
                }
            }
            notFindPage = { route: String -> NotFindPage(route) }
            isAnimation = true
            isDebug = BuildConfig.DEBUG
        }
    }
}

