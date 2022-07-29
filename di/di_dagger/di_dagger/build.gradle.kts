plugins {
    androidLibraryConvention
}

dependencies {
    implementation(project(":di:di"))
    implementation(project(":di:di_dagger:di_dagger_common"))
    implementation(project(":di:di_dagger:di_dagger_doorbell_list"))
    implementation(project(":di:di_dagger:di_dagger_image_details"))
    implementation(project(":di:di_dagger:di_dagger_image_list"))
    implementation(project(":di:di_dagger:di_dagger_permissions"))
    implementation(project(":di:di_dagger:di_dagger_splash"))

    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":data:dataDoorbellApiFirebase"))
    implementation(project(":common:common"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_persistence"))
    implementation(project(":base_cache"))
    implementation(project(":base_work_manager"))
    implementation(project(":ui:ui_splash"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":navigation"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.navigationFragmentKtx)

    implementation(Libraries.workRuntimeKtx)

    // dagger
    implementation(Libraries.dagger)
}