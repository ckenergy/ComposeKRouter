package com.ckenergy.compose.krouter.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

/**
 * @author ckenergy
 * @date 2023/6/29
 * @desc
 */
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.animationCompose(destination: NavGraphDestination) {
    composable(destination.route, destination.arguments, content = { it2 ->
        destination.content(it2)
    })
}