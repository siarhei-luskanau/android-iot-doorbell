plugins {
    androidLibraryConvention
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.doorbelllist"
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":ui:ui_common"))

    implementation(libs.android.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.paging.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.timber)

    // unit test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    // android test
    androidTestImplementation(libs.androidx.fragment.testing)
    androidTestImplementation(libs.androidx.paging.testing)
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(project(":common:common_test_ui"))
}
