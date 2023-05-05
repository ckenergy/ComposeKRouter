package com.ckenergy.compose.common

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import com.ckenergy.compose.safeargs.service.*
import com.google.accompanist.navigation.animation.composable


/**
 * Created by chengkai on 2022/12/1.
 */
fun Modifier.mainBackground(radius: Dp = 10.dp): Modifier {
    return background(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFF9242),
                Color(0xFFFF753F),
            ),
        ),
        shape = RoundedCornerShape(radius)
    )
}

@Composable
fun MainButton(text: String, modifier: Modifier, radius: Dp = 10.dp, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(50.dp)
            .mainBackground(radius)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableSafeArgs(route: String, content: @Composable (NavBackStackEntry, SafeArgsParser) -> Unit) {
    composableHorizontal(
        SafeArgsSource.getRoute(route),
        arguments = SafeArgsSource.getArguments()
    ) {
        val parser = it.parseSafeArgs()
        content(it, parser)
    }
}

@ExperimentalAnimationApi
inline fun <reified T> NavGraphBuilder.composable(crossinline content: @Composable (NavBackStackEntry, T?) -> Unit) {
    val provider = DestinationManager.getDestinationProvider(T::class.java)
    composableHorizontal(
        route = provider.getRoute(),
        arguments = provider.getArguments()
    ) {
        val deviceFilterBean: T? = it.parseArguments()
        content(it, deviceFilterBean)
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composableHorizontal(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    val springSpec = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    )

    this@composableHorizontal.composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
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
        content = content,
    )
}

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