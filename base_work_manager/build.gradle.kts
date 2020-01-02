plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"].toString().toInt())
    buildToolsVersion = rootProject.extra["buildToolsVersion"].toString()

    defaultConfig {
        minSdkVersion(rootProject.extra["minSdkVersion"].toString().toInt())
        targetSdkVersion(rootProject.extra["targetSdkVersion"].toString().toInt())
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common:common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")

    implementation("io.reactivex.rxjava2:rxjava:${rootProject.extra["rxJavaVersion"]}")
    implementation("io.reactivex.rxjava2:rxkotlin:${rootProject.extra["rxKotlinVersion"]}")

    implementation("androidx.work:work-runtime-ktx:${rootProject.extra["workManagerVersion"]}")
    implementation("androidx.work:work-gcm:${rootProject.extra["workManagerVersion"]}")
}