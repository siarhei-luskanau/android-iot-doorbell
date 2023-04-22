plugins {
    androidLibraryConvention
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.splash"
}

dependencies {
    implementation(project(":ui:ui_common"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.timber)

    // unit test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk)

    // android test
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(project(":common:common_test_ui"))
}
