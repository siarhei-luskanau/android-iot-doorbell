plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.repository"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.gitlive.firebase.storage)
        }
    }
}
