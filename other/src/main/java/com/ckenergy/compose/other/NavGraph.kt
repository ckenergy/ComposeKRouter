package com.ckenergy.compose.other

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.plugin.core.DestinationProvider
import com.ckenergy.compose.plugin.core.composeModules
import com.ckenergy.compose.plugin.core.navigateRoute
import com.ckenergy.compose.plugin.core.parseArguments

val navGraph = composeModules { controller ->
    packageName = "com.ckenergy.compose.other"
    composable(ComposeRouterMapper.Other) {
        OtherPage {
            controller.navigateRoute(ComposeRouterMapper.Second)
        }
    }
}