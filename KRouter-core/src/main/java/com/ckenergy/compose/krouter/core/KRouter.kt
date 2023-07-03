package com.ckenergy.compose.krouter.core

/**
 * @author ckenergy
 * @date 2023/6/25
 * @desc
 */
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION)
annotation class KRouter(
    /**
     * 路由的名字
     */
    val routeName: String )
