plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.android"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.paging.compose)
        }
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(libs.androidx.paging.runtime.ktx)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.timber)
        }
    }
}
