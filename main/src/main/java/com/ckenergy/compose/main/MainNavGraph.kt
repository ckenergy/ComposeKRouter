package com.ckenergy.compose.main

import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.plugin.core.composeModules
import com.ckenergy.compose.plugin.core.navigate1

val mainNavGraph = composeModules { controller ->
    composable(ComposeRouterMapper.Main.url) {
        MainPage {
            controller.navigate1(ComposeRouterMapper.Second.url)
        }
    }
    composable(ComposeRouterMapper.Second.url) {
        SecondPage {
            controller.navigate1(ComposeRouterMapper.Other.url)
        }
    }
}