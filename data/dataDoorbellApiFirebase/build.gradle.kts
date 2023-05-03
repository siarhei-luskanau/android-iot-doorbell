plugins {
    multiplatformConvention
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.firebase"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.firebase.storage)
                implementation(libs.gitlive.firebase.database)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":data:dataDoorbellApi"))
            }
        }
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
}
