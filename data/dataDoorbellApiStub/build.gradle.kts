plugins {
    id("multiplatformConvention")
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.repository.stub"

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":data:dataDoorbellApi"))
            implementation(libs.gitlive.firebase.storage)
        }
    }
}
