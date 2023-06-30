package com.ckenergy.compose.plugin

import android.app.Application
import com.ckenergy.compose.plugin.core.NavGraphManager

/**
 * @author ckenergy
 * @date 2023/5/5
 * @desc
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
//        NavGraphManager.registerDex(this)
    }

}