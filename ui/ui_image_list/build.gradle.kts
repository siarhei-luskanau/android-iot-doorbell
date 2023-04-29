plugins {
    multiplatformConvention
    id("androidx.navigation.safeargs.kotlin")
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.imagelist"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":ui:ui_common"))
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.navigation.ui.ktx)
                implementation(libs.timber)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.fragment.testing)
                implementation(libs.androidx.paging.testing)
                implementation(libs.androidx.test.core)
                implementation(libs.espresso.core)
                implementation(libs.kotlin.test)
                implementation(libs.mockk.android)
                implementation(project(":common:common_test_ui"))
            }
        }
    }
}
