plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.camera"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))

    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.androidxCameraCamerae2)
    implementation(Libraries.androidxCameraLifecycle)
    implementation(Libraries.androidxCameraView)
    implementation(Libraries.androidxCameraExtensions)
    implementation(Libraries.lifecycleProcess)
}
