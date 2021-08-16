plugins {
    id("android-library-convention")
    kotlin("plugin.serialization") version PublicVersions.kotlin
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