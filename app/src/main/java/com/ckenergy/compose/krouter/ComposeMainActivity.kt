package com.ckenergy.compose.krouter

import androidx.compose.runtime.Composable
import com.ckenergy.compose.BaseComposeActivity
import com.ckenergy.compose.NavGraph
import com.ckenergy.compose.common.ComposeRouterMapper

/**
 * Created by chengkai on 2022/10/28.
 */
class ComposeMainActivity : BaseComposeActivity() {

    @Composable
    override fun Content() {
        NavGraph(this, ComposeRouterMapper.Main) {
            finish()
        }
    }

}