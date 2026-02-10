plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.file"

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":common:common"))
        }
    }
}
