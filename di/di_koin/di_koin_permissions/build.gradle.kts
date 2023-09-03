plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.koin.permissions"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
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
}
