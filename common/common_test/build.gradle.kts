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
}

dependencies {
    implementation(project(":common:common"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["kotlinxCoroutinesVersion"]}")

    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:${rootProject.extra["lifecycleVersion"]}")

    implementation("org.spekframework.spek2:spek-dsl-jvm:${rootProject.extra["spekVersion"]}")

    implementation("io.reactivex.rxjava2:rxjava:${rootProject.extra["rxJavaVersion"]}")
    implementation("io.reactivex.rxjava2:rxkotlin:${rootProject.extra["rxKotlinVersion"]}")
}
