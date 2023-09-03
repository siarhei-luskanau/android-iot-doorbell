plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.di"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.work.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
