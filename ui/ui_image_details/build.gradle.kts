plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.ui.imagedetails"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":ui:ui_common"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.zoomable)
        }
    }
}
