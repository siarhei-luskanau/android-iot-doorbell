plugins {
    id("com.android.library")
    kotlin("android")
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)

    // koin
    implementation(Libraries.koinAndroid)
}
