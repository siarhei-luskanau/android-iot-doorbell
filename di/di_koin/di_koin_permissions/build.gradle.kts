plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.koin.permissions"
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":di:di_koin:di_koin_common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_permissions"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.koin.android)
    implementation(libs.kotlinx.coroutines.core)
}
