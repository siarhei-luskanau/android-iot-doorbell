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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":app:dagger:dagger_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.fragmentKtx)

    // dagger
    kapt(Libraries.daggerCompiler)
    implementation(Libraries.dagger)
}
