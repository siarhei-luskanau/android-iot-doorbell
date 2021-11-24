plugins {
    id("android-library-convention")
    id("androidx.navigation.safeargs.kotlin")
}

dependencies {
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    // implementation(Libraries.composeUiTooling)
    // implementation(Libraries.composeMaterial)
    // implementation(Libraries.composeUi)
    implementation(Libraries.navigationUiKtx)

    // unit test
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoCore)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.fragmentTesting)
}
