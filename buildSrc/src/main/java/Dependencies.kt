import Versions.safeArgs

object Versions {
    const val appcompat = "1.5.1"
    const val androidx = "1.8.0"
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 30

    const val retrofitVersion = "2.9.0"
    const val safeArgs = "1.0.2"
    const val room = "2.4.3"

    const val composeCompiler = "1.4.1"

}

object Libs {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val core_ktx = "androidx.core:core-ktx:1.7.0"
    const val core = "androidx.core:core:${Versions.androidx}"
    const val androidx_annotation = "androidx.annotation:annotation:1.5.0"
    const val material = "com.google.android.material:material:1.6.1"
    const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.4"
    const val lifecycle_runtime = "androidx.lifecycle:lifecycle-runtime-ktx:2.5.1"
    const val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0"
    const val lifecycle_viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1"
    const val gson = "com.google.code.gson:gson:2.10.1"
    const val mmkv = "com.tencent:mmkv:1.2.15"
    const val utilcode = "com.blankj:utilcode:1.30.6"
    const val koin = "io.insert-koin:koin-android:3.3.3"
    const val safeArgs_service = "io.github.ckenergy.compose.safeargs:service:$safeArgs"
    const val safeArgs_compiler = "io.github.ckenergy.compose.safeargs:compiler:$safeArgs"

    const val room_common = "androidx.room:room-common:${Versions.room}"
    // optional - room kotlin 扩展
    const val room_ktx = "androidx.room:room-ktx:${Versions.room}"
    const val room_compiler = "androidx.room:room-compiler:${Versions.room}"

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}"
    const val retrofit_gson = "com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}"
    const val retrofit_rxjava = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofitVersion}"

    const val bom_compose = "androidx.compose:compose-bom:2023.01.00"
    const val foundation_compose = "androidx.compose.foundation:foundation"
    const val activity_compose = "androidx.activity:activity-compose"
    const val material_compose = "androidx.compose.material:material"
    const val material3_compose = "androidx.compose.material3:material3"
    const val animation_compose = "androidx.compose.animation:animation"
    const val uiTooling_compose = "androidx.compose.ui:ui-tooling"
    const val livedata_compose = "androidx.compose.runtime:runtime-livedata"
    const val lifecycle_viewmodel_compose = "androidx.lifecycle:lifecycle-viewmodel-compose"

    const val koin_compose = "io.insert-koin:koin-androidx-compose:3.4.2"
    const val paging_compose = "androidx.paging:paging-compose:1.0.0-alpha17"
    const val lottie_compose = "com.airbnb.android:lottie-compose:5.2.0"
    const val constraintlayout_compose = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
    const val navigation_animation_compose = "com.google.accompanist:accompanist-navigation-animation:0.27.0"
    const val flowlayout_compose = "com.google.accompanist:accompanist-flowlayout:0.29.1-alpha"

    const val glide = "com.github.bumptech.glide:glide:4.14.2"

}