plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.camera"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.extensions)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.lifecycle.process)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.timber)
        }
    }
}
