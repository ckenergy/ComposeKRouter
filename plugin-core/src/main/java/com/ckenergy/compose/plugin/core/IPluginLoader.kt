package com.ckenergy.compose.plugin.core

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

/**
 * Copyright © Yeahka All Rights Reserved.
 * @author ckenergy
 * @date 2023/5/5
 * @desc
 */
interface IPluginLoader {

    fun load(route: String, controller: NavController, builder: NavGraphBuilder)

}