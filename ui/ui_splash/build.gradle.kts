plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.ui.splash"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":ui:ui_common"))
        }
    }
}
