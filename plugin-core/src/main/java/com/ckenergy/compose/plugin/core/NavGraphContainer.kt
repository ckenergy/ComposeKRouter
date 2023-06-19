package com.ckenergy.compose.plugin.core

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

typealias ModuleBuilder = NavGraphContainer.(NavController) -> Unit

class NavGraphContainer {
    internal val list = arrayListOf<NavGraphDestination>()

    var packageName = ""

    fun composable(
        route: String,
        arguments: List<NamedNavArgument> = emptyList(),
        content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit,
    ) {
        list.add(NavGraphDestination(route, arguments, content))
    }
    
}