package com.ckenergy.compose.other

import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.plugin.core.composeModules
import com.ckenergy.compose.plugin.core.navigate1

val libNavGraph = composeModules { controller ->
    composable(ComposeRouterMapper.Other.url) {
        OtherPage {
            controller.navigate1(ComposeRouterMapper.Second.url)
        }
    }
}