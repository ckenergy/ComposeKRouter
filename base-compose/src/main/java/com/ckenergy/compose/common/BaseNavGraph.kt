package com.ckenergy.compose.common

import com.ckenergy.compose.plugin.core.composeModules

/**
 * @author ckenergy
 * @date 2023/2/15
 * @desc 默认路由配置
 */
val baseNavGraph = composeModules {
    composable(ComposeRouterMapper.Empty.url) {
        EmptyPage()
    }
}