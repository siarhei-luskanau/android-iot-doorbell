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
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))
    implementation(project(":base_android"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_firebase"))
    implementation(project(":base_persistence"))
    implementation(project(":base_cache"))
    implementation(project(":base_work_manager"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.timber)

    implementation(Libraries.fragmentKtx)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.workRuntimeKtx)

    // dagger
    kapt(Libraries.daggerCompiler)
    implementation(Libraries.dagger)
}
