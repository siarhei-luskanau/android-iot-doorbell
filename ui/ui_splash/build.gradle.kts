plugins {
    multiplatformConvention
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.compose.screenshot)
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.splash"
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

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
