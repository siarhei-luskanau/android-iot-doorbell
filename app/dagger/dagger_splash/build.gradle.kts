plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_splash"))
    implementation(project(":app:dagger:dagger_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)

    // dagger
    kapt(Libraries.daggerCompiler)
    implementation(Libraries.dagger)
}
