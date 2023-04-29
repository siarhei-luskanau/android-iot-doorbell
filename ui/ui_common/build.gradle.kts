plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.common"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.android.material)
                implementation(libs.androidx.activity.ktx)
                implementation(libs.androidx.fragment.ktx)
                implementation(libs.androidx.paging.common.ktx)
                implementation(libs.androidx.paging.compose)
                implementation(libs.androidx.paging.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}
