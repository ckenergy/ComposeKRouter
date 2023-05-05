package com.ckenergy.compose

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ckenergy.compose.plugin.core.AppNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * Created by chengkai on 2022/11/28.
 */

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