plugins {
    id("multiplatformConvention")
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidLibrary.namespace = "siarhei.luskanau.iot.doorbell.data.firebase"

    sourceSets {
        commonMain.dependencies {
            implementation(libs.gitlive.firebase.database)
            implementation(libs.gitlive.firebase.storage)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":data:dataDoorbellApi"))
        }
    }
}
