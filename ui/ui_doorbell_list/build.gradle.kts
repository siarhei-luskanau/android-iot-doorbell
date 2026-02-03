plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.doorbelllist"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":ui:ui_common"))
                implementation(libs.androidx.paging.compose)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
