plugins {
    androidLibraryConvention
    kotlin("plugin.serialization") version PublicVersions.kotlin
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.data.firebase"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)

    implementation(Libraries.firebaseDatabaseKtx)
    implementation(Libraries.firebaseStorageKtx)

    implementation(Libraries.kotlinxSerializationJson)
    implementation(Libraries.gson)
}