plugins {
    androidLibraryConvention
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.permissions"
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":data:dataDoorbellApi"))

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
