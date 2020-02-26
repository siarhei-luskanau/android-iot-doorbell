plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(BuildVersions.compileSdkVersion)
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdkVersion(BuildVersions.minSdkVersion)
        targetSdkVersion(BuildVersions.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_image_details"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.timber)
    implementation(Libraries.material)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.navigationFragmentKtx)
}