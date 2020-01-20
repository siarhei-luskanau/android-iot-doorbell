plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdkVersion(BuildVersions.minSdkVersion)
        targetSdkVersion(BuildVersions.targetSdkVersion)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(TestLibraries.kotlinxCoroutinesTest)

    implementation(Libraries.lifecycleReactivestreamsKtx)

    implementation(TestLibraries.spekDslJvm)

    implementation(Libraries.rxJava)
    implementation(Libraries.rxKotlin)
}
