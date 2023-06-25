val kspVersion: String by project
val kotlinVersion: String by project

apply {
        from("../upload_maven.gradle")
}

plugins {
    kotlin("jvm")
}

group = "com.example"
version = "1.0-SNAPSHOT"

//repositories {
//    mavenCentral()
//}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-ksp:1.12.0")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

