plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.workmanager"

    sourceSets {
        androidMain.dependencies {
            implementation(project(":common:common"))
            implementation(project(":data:dataDoorbellApi"))
            implementation(libs.androidx.startup.runtime)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.gitlive.firebase.storage)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.timber)
        }
    }
}
