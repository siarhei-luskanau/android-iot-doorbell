plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.ui.common"

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.paging.common.ktx)
            implementation(libs.androidx.paging.compose)
            implementation(libs.androidx.paging.runtime.ktx)
        }
    }
}
