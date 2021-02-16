import de.mannodermaus.gradle.plugins.junit5.junitPlatform

plugins {
    id("com.android.library")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
}

android.testOptions.junitPlatform.jacocoOptions.taskGenerationEnabled = false

dependencies {
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.composeUiTooling)
    implementation(Libraries.composeMaterial)
    implementation(Libraries.composeUi)
    implementation(Libraries.navigationUiKtx)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoCore)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.fragmentTesting)
}
