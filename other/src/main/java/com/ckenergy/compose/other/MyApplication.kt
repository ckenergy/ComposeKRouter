package com.ckenergy.compose.other

import android.app.Application
import android.util.Log

/**
 * @author ckenergy
 * @date 2023/5/5
 * @desc
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("ss", "ss")
    }

}