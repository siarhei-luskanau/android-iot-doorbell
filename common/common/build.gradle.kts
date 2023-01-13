plugins {
    androidLibraryConvention
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.common"
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)
}
