plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)

    implementation(Libraries.firebaseDatabaseKtx)
    implementation(Libraries.firebaseStorageKtx)

    implementation(Libraries.moshiKotlin)
    kapt(Libraries.moshiCodegen)
}