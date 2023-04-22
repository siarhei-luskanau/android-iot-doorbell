plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.di"
}

dependencies {
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.kotlinx.coroutines.core)
}
