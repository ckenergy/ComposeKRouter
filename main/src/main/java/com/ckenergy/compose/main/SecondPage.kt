package com.ckenergy.compose.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ckenergy.compose.AppNavController
import com.ckenergy.compose.MyContent
import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.plugin.core.KRouter
import com.ckenergy.compose.plugin.core.ModuleBuilder
import com.ckenergy.compose.plugin.core.NavGraphManager
import com.ckenergy.compose.plugin.core.PluginManager
import com.ckenergy.compose.plugin.core.navigateRoute
import java.io.File

/**
 * @author ckenergy
 * @date 2023/5/4
 * @desc
 */
@KRouter(ComposeRouterMapper.Second)
@Composable
fun SecondPage(int: Int, string: String? = "default", testBean: TestBean, vararg long: Long) {
    Log.d("SecondPage", "int:$int,string:$string,testBean:$testBean, long:$long")
    MyContent(
        modifier = Modifier.background(Color.White),
        title = string ?: ""
    ) {
        val context = LocalContext.current
        val controller = AppNavController.current
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "load", fontSize = 20.sp, modifier = Modifier
                .padding(10.dp)
                .clickable {
                    loadApk(context)
                    addRoute(context, controller)
                }
                .padding(10.dp))
            Text(text = "next", fontSize = 20.sp, modifier = Modifier
                .padding(10.dp)
                .clickable {
                    controller.navigateRoute(ComposeRouterMapper.Other)
                }
                .padding(10.dp))
        }
    }

}

private fun loadApk(context: Context) {
    try {
        val patchDir: File = context.getDir("patch", Context.MODE_PRIVATE)
        val hotFixFile = File(patchDir, "other-debug.apk")
        hotFixFile.writeBytes(context.assets.open("other-debug.apk").readBytes())
        PluginManager.loadApk(context, hotFixFile.absolutePath, Constants.OTHER_PKG)
        Toast.makeText(context, "load apk success", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun addRoute(context: Context, controller: NavController) {
    try {
        val cls =
            PluginManager.getPluginInfo(Constants.OTHER_PKG)?.pluginDexClassLoader!!.loadClass(
                Constants.OTHER_PKG+".NavGraphKt"
            )
        val graph = cls.declaredMethods.first().invoke(null) as ModuleBuilder
        NavGraphManager.composablePlugIn(controller, graph)
        Toast.makeText(context, "add route success", Toast.LENGTH_SHORT).show()
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: InstantiationException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }
}