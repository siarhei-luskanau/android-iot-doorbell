plugins {
    androidLibraryConvention
    kotlin("kapt")
}

dependencies {
    implementation(project(":di:di_dagger:di_dagger_common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_splash"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)

    // dagger
    kapt(Libraries.daggerCompiler)
    implementation(Libraries.dagger)
}
