plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.di"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":navigation"))
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
