plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.di.koin"
}

dependencies {
    implementation(project(":di:di"))
    implementation(project(":di:di_koin:di_koin_common"))
    implementation(project(":di:di_koin:di_koin_doorbell_list"))
    implementation(project(":di:di_koin:di_koin_image_details"))
    implementation(project(":di:di_koin:di_koin_image_list"))
    implementation(project(":di:di_koin:di_koin_permissions"))
    implementation(project(":di:di_koin:di_koin_splash"))

    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":data:dataDoorbellApiFirebase"))
    implementation(project(":data:dataDoorbellApiStub"))
    implementation(project(":common:common"))
    implementation(project(":base_android"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_persistence"))
    implementation(project(":base_work_manager"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_splash"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":navigation"))

    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.navigationFragmentKtx)

    implementation(Libraries.androidxStartup)
    implementation(Libraries.workRuntimeKtx)

    // koin
    implementation(Libraries.koinAndroid)
}
