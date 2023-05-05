package com.ckenergy.compose.mylibrary

import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.plugin.core.composeModules

val libNavGraph = composeModules { controller ->
    composable(ComposeRouterMapper.Library.url) {
        LibraryPage {
//                controller.navigate(ComposeRouterMapper.Second.url)
        }
    }
}