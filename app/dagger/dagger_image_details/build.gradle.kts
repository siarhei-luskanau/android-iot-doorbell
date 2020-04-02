plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":app:dagger:dagger_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.navigationUiKtx)

    // dagger
    kapt(Libraries.daggerCompiler)
    implementation(Libraries.dagger)
}
