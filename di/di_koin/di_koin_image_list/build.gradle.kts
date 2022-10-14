plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.koin.imagelist"
}

dependencies {
    implementation(project(":di:di_koin:di_koin_common"))
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_image_list"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.workRuntimeKtx)
    implementation(Libraries.navigationUiKtx)

    // koin
    implementation(Libraries.koinAndroid)
}