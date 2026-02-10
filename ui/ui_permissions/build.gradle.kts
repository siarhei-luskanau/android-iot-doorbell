plugins {
    id("multiplatformConvention")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.ui.permissions"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":ui:ui_common"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.moko.permissions.camera)
            implementation(libs.moko.permissions.compose)
        }
    }
}
