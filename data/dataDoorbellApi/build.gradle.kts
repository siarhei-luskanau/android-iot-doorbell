plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.data.repository"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.gitlive.firebase.storage)
        }
    }
}
