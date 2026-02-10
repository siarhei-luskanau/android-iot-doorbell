plugins {
    id("multiplatformConvention")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.splash"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":ui:ui_common"))
        }
    }
}
