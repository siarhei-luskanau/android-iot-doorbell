plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.common"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.runtime.ktx)
            }
        }
    }
}
