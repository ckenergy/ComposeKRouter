plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android.sourceSets.all {
    java.srcDir("build/generated/ksp/${name}/kotlin/") }

android {
    namespace = "com.ckenergy.compose.main"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

ksp {
    arg("KROUTER_PACKAGE", "com.ckenergy.compose.main")
}

dependencies {

    implementation(Libs.core_ktx)
    implementation(Libs.core)
    implementation(Libs.appcompat)
    implementation(Libs.material)

    implementation (project(":base-compose"))
    "ksp"(project(":KRouter-compiler"))
}