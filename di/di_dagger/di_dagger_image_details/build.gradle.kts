plugins {
    androidLibraryConvention
    kotlin("kapt")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.dagger.imagedetails"
}

dependencies {
    implementation(project(":di:di_dagger:di_dagger_common"))
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_image_details"))

    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.navigationUiKtx)

    // dagger
    kapt(Libraries.daggerCompiler)
    implementation(Libraries.dagger)
}
