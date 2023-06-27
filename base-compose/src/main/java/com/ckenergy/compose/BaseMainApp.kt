package com.ckenergy.compose

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * Created by chengkai on 2022/11/28.
 */

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BaseMainApp(content: @Composable () -> Unit) {
    val navController = rememberAnimatedNavController()
    //把navController 赋给 AppNavController
    CompositionLocalProvider(
        AppNavController provides navController,
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