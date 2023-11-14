package com.ckenergy.compose.krouter.core

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

val S_GSON = Gson()

const val argName = "args"

/**
 * Created by chengkai on 2022/11/10.
 */
object DestinationProvider {

    fun getDestination(route1: String, source: Any): String {
        return "${route1}/${URLEncoder.encode(S_GSON.toJson(source), "utf-8")}"
    }

}

fun getArguments(): MutableList<NamedNavArgument> {
    return arrayListOf(
        navArgument(argName) {
            type = NavType.StringType
            nullable = true
        })
}

inline fun <reified T> NavBackStackEntry.parseArguments(): T? {
    val data = arguments?.getString(argName)
    return data?.let {
        var t = kotlin.runCatching {
            S_GSON.fromJson(data, T::class.java)
        }.getOrNull()
        if (t == null) {
            t = kotlin.runCatching {
                S_GSON.fromJson(URLDecoder.decode(it, "utf-8"), T::class.java)
            }.getOrNull()
        }
        t
    }
}