package com.ckenergy.compose.krouter.core

import androidx.navigation.NavType
import androidx.navigation.navArgument

const val NOT_FIND_ROUTE = "Not_Find_Route="
/**
 * @author ckenergy
 * @date 2023/2/15
 * @desc 默认路由配置
 */
val baseNavGraph = composeModules {
    packageName = "com.ckenergy.compose.krouter.core"
    composable(
        "$NOT_FIND_ROUTE{args}", arguments = listOf(
        navArgument("args") {
            type = NavType.StringType   //参数类型
            defaultValue = ""        //默认值
            nullable = true          //是否可空
        }
    )) {
        val route = it.arguments?.getString("args") ?: ""
        NavGraphManager.notFindPage?.invoke(route)
    }
}