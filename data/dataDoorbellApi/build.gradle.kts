plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.repository"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.gitlive.firebase.storage)
            }
        }
    }
}
