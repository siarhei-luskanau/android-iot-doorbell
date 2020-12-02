plugins {
    id("com.android.library")
    kotlin("android")
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.kotlinxDatetime)

    // android test, but implementation
    implementation(TestLibraries.kotlinTest)
    implementation(TestLibraries.testEspressoCore)
    implementation(TestLibraries.testEspressoIntents)
    implementation(TestLibraries.testEspressoContrib)
    implementation(TestLibraries.androidTestCoreKtx)
    implementation(TestLibraries.androidTestExtTruth)
    implementation(TestLibraries.testExtJunitKtx)
}
