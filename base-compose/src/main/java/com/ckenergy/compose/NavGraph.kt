package com.ckenergy.compose

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.*
import com.ckenergy.compose.safeargs.service.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.ckenergy.compose.plugin.core.NavGraphManager
import com.ckenergy.compose.common.ComposeRouterMapper

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = ComposeRouterMapper.Empty.url,
    finishActivity: (Boolean) -> Unit = {}
) {
    val navController = AppNavController.current
    val route = startDestination.ifBlank { ComposeRouterMapper.Empty.url }

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
        NavGraphManager.composable(this, navController)
    }
}

