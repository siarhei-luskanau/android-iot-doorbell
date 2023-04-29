plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.common"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":data:dataDoorbellApi"))
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}
