plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.android"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":common:common"))
            implementation(libs.androidx.paging.common.ktx)
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.timber)
        }
    }
}
