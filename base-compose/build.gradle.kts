@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.ckenergy.base.compose"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        resourcePrefix = "compose"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }
}

dependencies {
    implementation(Libs.core_ktx)
    implementation(Libs.core)
    implementation(Libs.appcompat)
    implementation(Libs.material)

    val composeBom = platform(Libs.bom_compose)
    api(composeBom)

    api(Libs.activity_compose)
    api(Libs.animation_compose)
    api(Libs.lifecycle_viewmodel_compose)
    api(Libs.material3_compose)
    api(Libs.constraintlayout_compose)
    api(Libs.foundation_compose)
    api(Libs.uiTooling_compose)
    api(Libs.paging_compose)
    api(Libs.lottie_compose)
    api(Libs.livedata_compose)

    api(Libs.navigation_animation_compose)
    api(Libs.flowlayout_compose)
    api(project(":KRouter-core"))

}