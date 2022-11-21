plugins {
    androidLibraryConvention
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.imagedetails"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.navigationUiKtx)

    // unit test
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockkCore)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.mockkAndroid)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.fragmentTesting)
}
