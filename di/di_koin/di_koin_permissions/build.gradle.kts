plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.koin.permissions"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":di:di_koin:di_koin_common"))
            implementation(project(":ui:ui_common"))
            implementation(project(":ui:ui_permissions"))
            implementation(libs.koin.android)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
