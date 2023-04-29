plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.workmanager"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(libs.androidx.startup.runtime)
                implementation(libs.androidx.work.runtime.ktx)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.timber)
            }
        }
    }
}
