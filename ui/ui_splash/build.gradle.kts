plugins {
    multiplatformConvention
    id("androidx.navigation.safeargs.kotlin")
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.splash"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":ui:ui_common"))
                implementation(libs.androidx.navigation.ui.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}
