package com.ckenergy.compose

import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.navOptions

/**
 * 配置好的跳转方法
 */
class MainActions(private val navController: NavHostController, finish: (Boolean, Intent?) -> Unit) {

    /**
     * 返回上一级
     */
    val upPress: () -> Unit = {
        if (!navController.popBackStack()) {
            finish(false, null)
        }
    }

    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }

    val upPressWithOk: (Intent) -> Unit = {
        finish(true, it)
    }

    fun navigate(route: String) {
        navController.navigate(route, navOptions {  })
    }

    private fun NavBackStackEntry.navigate(route: String) {
        navigate {
            navController.navigate(route)
        }
    }

    private fun NavBackStackEntry.navigate(navigate: () -> Unit) {
        if (lifecycleIsResumed()) {
            navigate()
        }
    }

}

fun NavHostController.navigateIsResumed(entry: NavBackStackEntry, route: String) {
    if (entry.lifecycleIsResumed()) {
        navigate(route)
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED