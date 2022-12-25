plugins {
    androidLibraryConvention
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.imagelist"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))

    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.material)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingCompose)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.timber)

    // unit test
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)
    testImplementation(TestLibraries.mockkCore)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.fragmentTesting)
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.mockkAndroid)
    androidTestImplementation(TestLibraries.pagingTesting)
    androidTestImplementation(TestLibraries.testEspressoCore)
}
