plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.ui.common"

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
