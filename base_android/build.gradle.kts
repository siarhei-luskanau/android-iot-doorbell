plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.android"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":common:common"))
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}
