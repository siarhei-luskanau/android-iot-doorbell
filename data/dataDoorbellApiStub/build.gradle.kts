plugins {
    multiplatformConvention
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.data.repository.stub"

    sourceSets {
        commonMain.dependencies {
            implementation(project(":data:dataDoorbellApi"))
            implementation(libs.gitlive.firebase.storage)
        }
    }
}
