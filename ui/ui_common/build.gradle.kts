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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.coil)
}
