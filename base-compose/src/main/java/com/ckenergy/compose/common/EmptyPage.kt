package com.ckenergy.compose.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Created by chengkai on 2022/11/8.
 */
@Composable
fun EmptyPage() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
        Text(modifier = Modifier.padding(10.dp), text = "")
    }
}