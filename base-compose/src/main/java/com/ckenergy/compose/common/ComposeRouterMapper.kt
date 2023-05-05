package com.ckenergy.compose.common

/**
 * @author ckenergy
 * @date 2023/2/15
 * @desc
 */
sealed class ComposeRouterMapper(url: String): Router("compose/$url") {

    object Main: ComposeRouterMapper("main")
    object Second: ComposeRouterMapper("second")

    object Library: ComposeRouterMapper("library")

    object Empty : ComposeRouterMapper("empty")

}

sealed class Router(val url: String)