package com.ckenergy.compose.plugin

import android.app.Application
import com.ckenergy.compose.common.baseNavGraph
import com.ckenergy.compose.mylibrary.libNavGraph
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
    }

    private fun initCompose() {
        composeNav {
            addModules(mainNavGraph)
//            addModules(libNavGraph)
            addModules(baseNavGraph)
        }
    }

}