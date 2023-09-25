plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.repository.stub"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":data:dataDoorbellApi"))
                implementation(libs.gitlive.firebase.storage)
            }
        }
    }
}
