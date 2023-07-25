plugins {
    multiplatformConvention
    id("androidx.navigation.safeargs.kotlin")
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.permissions"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(libs.androidx.navigation.ui.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.androidx.fragment.testing)
                implementation(libs.androidx.test.core)
                implementation(libs.espresso.core)
                implementation(libs.kotlin.test)
                implementation(project(":common:common_test_ui"))
            }
        }
    }
}
