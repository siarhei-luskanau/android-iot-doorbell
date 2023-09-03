plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.koin.common"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":common:common"))
                implementation(project(":data:dataDoorbellApi"))
                implementation(libs.koin.android)
                implementation(libs.timber)
            }
        }
    }
}
