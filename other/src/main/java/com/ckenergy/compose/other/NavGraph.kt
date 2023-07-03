package com.ckenergy.compose.other

import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.krouter.core.composeModules
import com.ckenergy.compose.krouter.core.navigateRoute

val navGraph = composeModules { controller ->
    packageName = "com.ckenergy.compose.other"
    composable(ComposeRouterMapper.Other) {
        OtherPage {
            controller.navigateRoute(ComposeRouterMapper.Second)
        }
    }
}