plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.di"
}

dependencies {
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.workRuntimeKtx)
}
