plugins {
    id("multiplatformConvention")
    alias(libs.plugins.kotlinx.serialization)
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.firebase"

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.gitlive.firebase.database)
                implementation(libs.gitlive.firebase.storage)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":data:dataDoorbellApi"))
            }
        }
    }
}
