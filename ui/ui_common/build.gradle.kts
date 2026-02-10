plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.ui.common"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
        androidMain.dependencies {
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.androidx.paging.runtime.ktx)
        }
    }
}
