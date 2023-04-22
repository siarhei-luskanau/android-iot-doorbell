plugins {
    androidLibraryConvention
    kotlin("kapt")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.dagger.imagedetails"
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":di:di_dagger:di_dagger_common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_image_details"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.dagger)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.navigation.ui.ktx)
    kapt(libs.dagger.compiler)
}
