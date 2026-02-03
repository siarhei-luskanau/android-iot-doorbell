plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.file"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":data:dataDoorbellApi"))
                implementation(project(":common:common"))
            }
        }
    }
}
