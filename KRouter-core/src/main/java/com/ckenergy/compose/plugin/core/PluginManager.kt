package com.ckenergy.compose.plugin.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.Composable
import dalvik.system.DexClassLoader
import java.lang.reflect.Method


object PluginManager {
    private const val TAG = "PluginManager"

    val pluginInfoMap = hashMapOf<String, PluginInfo>()

    fun getResource(packageName: String): Resources? = pluginInfoMap[packageName]?.pluginRes

    fun getPluginInfo(packageName: String): PluginInfo? = pluginInfoMap[packageName]

    @SuppressLint("DiscouragedPrivateApi")
    fun loadApk(ctx: Context?, apkPath: String, packageName: String) {
        if (ctx == null) {
            Log.d(TAG, "ctx is null, apk cannot be loaded dynamically")
            throw RuntimeException("ctx is null, apk cannot be loaded dynamically")
        }
        try {
            val appCtx = ctx.applicationContext
            val pluginDexClassLoader = DexClassLoader(
                apkPath,
                appCtx!!.getDir("dex2opt", Context.MODE_PRIVATE).absolutePath,
                null,
                appCtx!!.classLoader
            )
            val pluginPackageArchiveInfo =
                appCtx!!.packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES)!!

            val pluginAssets = AssetManager::class.java.newInstance()
            val addAssetPath: Method =
                AssetManager::class.java.getDeclaredMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(pluginAssets, apkPath)
            val superResources: Resources? = ctx.resources
            val pluginRes = Resources(
                pluginAssets,
                superResources?.displayMetrics,
                superResources?.configuration
            )
            pluginInfoMap[packageName] = PluginInfo(packageName, appCtx, pluginAssets, pluginRes, pluginDexClassLoader, pluginPackageArchiveInfo)
            Log.d(TAG, "dynamic loading of apk success")
        } catch (e: Exception) {
            Log.d(TAG, "dynamic loading of apk failed")
            e.printStackTrace()
        }
    }

}