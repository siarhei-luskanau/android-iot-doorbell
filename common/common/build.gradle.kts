plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.common"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)
}
