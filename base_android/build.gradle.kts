plugins {
    androidLibraryConvention
    id("hu.supercluster.paperwork")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell.android"
}

paperwork {
    set = runCatching {
        mapOf(
            "gitSha" to gitSha(),
            "gitBranch" to gitBranch(),
            "buildDate" to buildTime("yyyy-MM-dd HH:mm:ss", "GMT")
        )
    }.getOrNull()
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)

    implementation(Libraries.paperwork)

    // unit test
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockkCore)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)
}