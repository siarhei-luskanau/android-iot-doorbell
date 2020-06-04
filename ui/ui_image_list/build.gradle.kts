plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.viewBindingPropertyDelegate)
    implementation(Libraries.material)
    implementation(Libraries.swiperefreshlayout)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(Libraries.lifecycleViewmodelKtx)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.coil)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.fragmentTesting)
    kaptAndroidTest(TestLibraries.databindingCompiler)
}
