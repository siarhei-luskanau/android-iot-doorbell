plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.firebaseDatabaseKtx)
    implementation(Libraries.firebaseStorageKtx)

    implementation(Libraries.moshiKotlin)
    kapt(Libraries.moshiCodegen)
}