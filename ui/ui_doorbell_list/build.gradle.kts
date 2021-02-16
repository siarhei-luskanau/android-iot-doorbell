import de.mannodermaus.gradle.plugins.junit5.junitPlatform

plugins {
    id("com.android.library")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
}

android.testOptions.junitPlatform.jacocoOptions.taskGenerationEnabled = false

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.swiperefreshlayout)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(Libraries.lifecycleViewmodelKtx)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.coil)

    // unit test
    testImplementation(project(":common:common_test"))
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoCore)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.mockitoAndroid)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.fragmentTesting)
}
