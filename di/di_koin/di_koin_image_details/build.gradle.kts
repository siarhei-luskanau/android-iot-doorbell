plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.koin.imagedetails"

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":di:di_koin:di_koin_common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":common:common"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_image_details"))
            implementation(libs.koin.android)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
