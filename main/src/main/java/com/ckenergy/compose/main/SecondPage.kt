package com.ckenergy.compose.plugin

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

/**
 * @author ckenergy
 * @date 2023/5/4
 * @desc
 */
@Composable
fun SecondPage(start: () -> Unit) {
    MyContent(
        modifier = Modifier.background(Color.White),
        title = "Second"
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = "next", fontSize = 20.sp, modifier = Modifier.padding(10.dp).clickable {
                start()
            }.padding(10.dp))
        }
    }

}