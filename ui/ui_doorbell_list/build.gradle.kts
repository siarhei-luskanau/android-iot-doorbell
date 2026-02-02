plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.ui.doorbelllist"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":ui:ui_common"))
            implementation(libs.androidx.paging.compose)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
