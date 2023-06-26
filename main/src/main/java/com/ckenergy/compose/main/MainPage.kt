package com.ckenergy.compose.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ckenergy.compose.MyContent
import com.ckenergy.compose.common.ComposeRouterMapper
import com.ckenergy.compose.plugin.core.AppNavController
import com.ckenergy.compose.plugin.core.KRouter
import com.ckenergy.compose.plugin.core.navigateRoute

/**
 * @author ckenergy
 * @date 2023/5/4
 * @desc
 */
@KRouter(ComposeRouterMapper.Main)
@Composable
fun MainPage() {
    val controller = AppNavController.current

    MyContent(
        modifier = Modifier.background(Color.White),
        title = "Main"
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "start", fontSize = 20.sp, modifier = Modifier
                .padding(10.dp)
                .clickable {
                    controller.navigateRoute(ComposeRouterMapper.Second) {
                        put("int", 1)
//                        put("string", "test")
                        put("long", longArrayOf(1L, 3L))
                        put("testBean", TestBean(1.5f, false))
                    }
                }
                .padding(10.dp))
        }
    }

}