plugins {
    androidLibraryConvention
    // kotlin("kapt")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.persistence"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    // kapt(Libraries.roomCompiler)
    implementation(Libraries.roomKtx)
}
