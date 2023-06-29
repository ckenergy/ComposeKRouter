plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android.sourceSets.all { java.srcDir("build/generated/ksp/${name}/kotlin/") }

android {
    namespace = "com.ckenergy.compose.other"
    compileSdk = Versions.compileSdk

    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        applicationId = "com.ckenergy.compose.other"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        baseline = file("lint-baseline.xml")
    }
    lintOptions {
        disable("Instantiatable")
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
    arg("KROUTER_PACKAGE", "com.ckenergy.compose.other")
}

dependencies {
    implementation(Libs.core_ktx)
    implementation(Libs.core)
    implementation(Libs.appcompat)
    implementation(Libs.material)

    implementation(project(":base-compose"))
    "ksp"(project(":KRouter-compiler"))
}