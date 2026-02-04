plugins {
    id("multiplatformConvention")
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.file"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":data:dataDoorbellApi"))
            implementation(project(":common:common"))
        }
    }
}
