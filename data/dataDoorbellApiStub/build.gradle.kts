plugins {
    multiplatformConvention
}

android.namespace = "siarhei.luskanau.iot.doorbell.data.repository.stub"

kotlin {
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":data:dataDoorbellApi"))
            }
        }
    }
}
