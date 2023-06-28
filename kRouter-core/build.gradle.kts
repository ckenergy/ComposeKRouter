plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.ckenergy.compose.plugin.core"
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
    val composeBom = platform(Libs.bom_compose)
    implementation(composeBom)

//    implementation("androidx.navigation:navigation-compose:2.6.0")

//    implementation(Libs.animation_compose)
//    implementation(Libs.foundation_compose)
    implementation(Libs.navigation_animation_compose)
    implementation(Libs.gson)
}