plugins {
    id("multiplatformConvention")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.splash"
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
