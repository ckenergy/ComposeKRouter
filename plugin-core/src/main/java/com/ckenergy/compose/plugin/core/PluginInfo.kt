package com.ckenergy.compose.plugin.core

import android.content.Context
import android.content.pm.PackageInfo
import android.content.res.AssetManager
import android.content.res.Resources
import dalvik.system.DexClassLoader

/**
 * @author ckenergy
 * @date 2023/5/6
 * @desc
 */
data class PluginInfo(
    /**
     * 插件apk的包名，用于resource的正确加载
     * */
    var pluginPackageName: String = "",
    var appCtx: Context? = null,
    var pluginAssets: AssetManager? = null,
    var pluginRes: Resources? = null,
    var pluginDexClassLoader: DexClassLoader? = null,
    var pluginPackageArchiveInfo: PackageInfo? = null,)