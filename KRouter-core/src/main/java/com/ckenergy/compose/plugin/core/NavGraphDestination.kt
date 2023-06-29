package com.ckenergy.compose.plugin.core

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry

/**
 * @author ckenergy
 * @date 2023/5/5
 * @desc
 */
data class NavGraphDestination(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val content: @Composable (NavBackStackEntry) -> Unit,
)