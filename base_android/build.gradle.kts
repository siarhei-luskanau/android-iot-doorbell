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
            "buildDate" to buildTime("yyyy-MM-dd HH:mm:ss", "GMT"),
        )
    }.getOrNull()
}

dependencies {
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":common:common"))

    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.paperwork)
    implementation(libs.timber)

    // unit test
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
}
