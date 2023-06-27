package com.ckenergy.compose.other

import android.app.Application
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.ckenergy.compose.plugin.core.IPluginLoader
import com.ckenergy.compose.plugin.core.composeNav

/**
 * @author ckenergy
 * @date 2023/5/5
 * @desc
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initCompose()
        Log.d("ss", "ss")
    }

    private fun initCompose() {
        composeNav {
            pluginLoader = object : IPluginLoader {
                override fun load(route: String, controller: NavController, builder: NavGraphBuilder) {
//                    composablePlugIn(controller, libNavGraph)
                }
            }
            notFindPage = { route: String -> NotFindPage(route) }
//            addModules(navGraph)
        }
    }

}