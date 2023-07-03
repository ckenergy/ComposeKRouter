package com.ckenergy.compose.other

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ckenergy.compose.AppNavController
import com.ckenergy.compose.MyContent
import com.ckenergy.compose.krouter.core.PluginManager

/**
 * @author ckenergy
 * @date 2023/5/4
 * @desc
 */
@Composable
fun OtherPage(start: () -> Unit) {
    Log.d("other", ""+AppNavController.current.currentDestination?.route)
    MyContent(
        modifier = Modifier.background(Color.White),
        title = "Library"
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(text = getText(R.string.test_res),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
                    .clickable {
                        start()
                    }
                    .padding(10.dp))
        }
    }

}

@Composable
private fun getText(id: Int): String {
    val pluginInfo = PluginManager.getPluginInfo("com.ckenergy.compose.other")
    return if (pluginInfo != null) pluginInfo.pluginRes?.getString(id) ?: "" else stringResource(
        id = id
    )
}
