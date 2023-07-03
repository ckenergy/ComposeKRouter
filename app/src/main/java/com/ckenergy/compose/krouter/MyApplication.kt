package com.ckenergy.compose.krouter

import android.app.Application

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