plugins {
    androidLibraryConvention
    kotlin("plugin.serialization") version PublicVersions.kotlin
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.data.firebase"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinxCoroutinesCore)

    implementation(platform(Libraries.firebaseBom))
    implementation(Libraries.firebaseDatabaseKtx)
    implementation(Libraries.firebaseStorageKtx)

    implementation(Libraries.kotlinxSerializationJson)
}
