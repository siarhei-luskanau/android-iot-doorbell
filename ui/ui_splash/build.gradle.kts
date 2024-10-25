plugins {
    multiplatformConvention
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
            }
        }
    }
}
