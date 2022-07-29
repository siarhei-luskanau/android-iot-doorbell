plugins {
    androidLibraryConvention
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.workRuntimeKtx)
    implementation(Libraries.androidxStartup)
}