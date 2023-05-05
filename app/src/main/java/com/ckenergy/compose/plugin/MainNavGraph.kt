package com.ckenergy.compose.plugin

import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.mylibrary.libNavGraph
import com.ckenergy.compose.plugin.core.NavGraphManager
import com.ckenergy.compose.plugin.core.composeModules

val mainNavGraph = composeModules { controller ->
    composable(ComposeRouterMapper.Main.url) {
        MainPage {
            controller.navigate(ComposeRouterMapper.Second.url)
        }
    }
    composable(ComposeRouterMapper.Second.url) {
        SecondPage {
            NavGraphManager.composablePlugIn(controller, libNavGraph)
            controller.navigate(ComposeRouterMapper.Library.url)
        }
    }
}