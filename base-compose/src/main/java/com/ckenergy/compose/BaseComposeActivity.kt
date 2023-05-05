package com.ckenergy.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ckenergy.base.compose.R
import com.ckenergy.compose.theme.ComposeTheme


/**
 *
 * Compose框架activity的基类内部定义了appBar，加载条
 * 非全屏使用[MyContent] 全屏使用[MyFullContent]
 *
 * Created by chengkai on 2022/10/28.
 */
abstract class BaseComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        edgeToEdge(true)
//        edgeSetSystemBarLight(true)
        setMyContent {
            BaseMainApp {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()
}

@Composable
fun MyContent(modifier: Modifier = Modifier,
              appBar: @Composable () -> Unit = {},
              content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier
        .fillMaxSize()
        .composed { modifier }) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        appBar()
        Divider(color = Color(0xFFDBDBDB), thickness = 0.5.dp)
        content()
    }
}

@Composable
fun MyContent(modifier: Modifier = Modifier,
              title: String,
              content: @Composable ColumnScope.() -> Unit) {
    MyContent(modifier, appBar = { MyAppBar(title = title) }, content)
}

@Composable
fun MyFullContent(
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        content()
        appBar()
    }
}

internal fun BaseComposeActivity.setMyContent(
    content: @Composable () -> Unit
) {
    setContent {
        ComposeTheme{
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                content()
            }
        }
    }
}

@Composable
fun MyAppBar(
    title: String,
    rightButton: @Composable BoxScope.() -> Unit = {},
    onBack: () -> Unit = LocalMainActions.current.upPress
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            text = title,
            color = Color.Black,
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
        )
        rightButton()
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = onBack
        ) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.mipmap.compose_ic_back),
                null
            )
        }
    }
}

@Composable
fun BoxScope.AppBarRight(text: String, click: () -> Unit) {
    Text(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .align(Alignment.CenterEnd)
            .clickable {
                click()
            },
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
    )
}

