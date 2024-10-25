plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.imagelist"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":ui:ui_common"))
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
