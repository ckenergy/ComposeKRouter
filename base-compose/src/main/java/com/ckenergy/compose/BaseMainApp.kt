package com.ckenergy.compose

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ckenergy.compose.MainActions

/**
 * Created by chengkai on 2022/11/28.
 */

//公用一个NavHostController 全局获取当前可跳转
@SuppressLint("CompositionLocalNaming")
val AppNavController = compositionLocalOf<NavHostController> { error("NavHostController error") }

//配置好的跳转路由
val LocalMainActions = compositionLocalOf<MainActions> { error("MainActions error") }

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseMainApp(content: @Composable () -> Unit) {
    val navController = rememberAnimatedNavController()
    val activity = LocalContext.current as? Activity
    val actions = remember(navController) {
        MainActions(navController) { result, intent ->
            activity?.let {
                if (result) it.setResult(Activity.RESULT_OK, intent)
                it.finish()
            }
        }
    }
    //把navController 赋给 AppNavController
    CompositionLocalProvider(
        AppNavController provides navController,
        LocalMainActions provides actions,
    ) {
        //输入框焦点管理器
//        val focusManager = LocalFocusManager.current
        //统一做点击空白处关闭键盘
//        Box(
//            Modifier
//                .fillMaxSize()
//                .clickable( 键盘的回车点击事件会透传到这里来，先注释。。。
//                    onClick = {
//                        focusManager.clearFocus()
//                    },
//                    indication = null,
//                    interactionSource = remember {
//                        MutableInteractionSource()
//                    })
//        ) {
//        }
        content.invoke()
    }
}