plugins {
    androidLibraryConvention
    kotlin("kapt")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.dagger.splash"
}

dependencies {
    implementation(project(":di:di_dagger:di_dagger_common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_splash"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.dagger)
    implementation(libs.kotlinx.coroutines.core)
    kapt(libs.dagger.compiler)
}
