plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.common"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
        androidMain.dependencies {
            implementation(project(":data:dataDoorbellApi"))
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.timber)
        }
    }
}
