plugins {
    androidLibraryConvention
    kotlin("plugin.serialization") version libs.versions.kotlin
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.data.firebase"
}

dependencies {
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.firebase.bom))
    implementation(project(":data:dataDoorbellApi"))
}
