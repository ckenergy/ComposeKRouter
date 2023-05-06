package com.ckenergy.compose.main

import android.util.Log
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
import com.ckenergy.compose.plugin.core.AppNavController

/**
 * @author ckenergy
 * @date 2023/5/4
 * @desc
 */
@Composable
fun MainPage(start: () -> Unit) {
    Log.d("MainPage", ""+ AppNavController.current.currentDestination?.route)
    MyContent(
        modifier = Modifier.background(Color.White),
        title = "Main"
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "start", fontSize = 20.sp, modifier = Modifier
                .padding(10.dp)
                .clickable {
                    start()
                }
                .padding(10.dp))
        }
    }

}