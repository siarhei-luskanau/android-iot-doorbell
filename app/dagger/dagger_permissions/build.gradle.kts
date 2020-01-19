plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
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

    viewBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":app:dagger:dagger_common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")
    implementation("androidx.fragment:fragment:${rootProject.extra["fragmentVersion"]}")

    // dagger
    kapt("com.google.dagger:dagger-compiler:${rootProject.extra["daggerVersion"]}")
    implementation("com.google.dagger:dagger:${rootProject.extra["daggerVersion"]}")
}
