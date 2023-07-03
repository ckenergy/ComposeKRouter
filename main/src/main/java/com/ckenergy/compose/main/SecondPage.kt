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
import com.ckenergy.compose.krouter.core.KRouter
import com.ckenergy.compose.krouter.core.ModuleBuilder
import com.ckenergy.compose.krouter.core.NavGraphManager
import com.ckenergy.compose.krouter.core.PluginManager
import com.ckenergy.compose.krouter.core.navigateRoute
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
        val controller = AppNavController.current
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "next", fontSize = 20.sp, modifier = Modifier
                .padding(10.dp)
                .clickable {
                    controller.navigateRoute(ComposeRouterMapper.Other)
                }
                .padding(10.dp))
        }
    }

}