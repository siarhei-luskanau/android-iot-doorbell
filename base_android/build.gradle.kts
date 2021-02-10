import de.mannodermaus.gradle.plugins.junit5.junitPlatform

plugins {
    id("com.android.library")
    kotlin("android")
    id("de.mannodermaus.android-junit5")
    id("hu.supercluster.paperwork")
}

android.testOptions.junitPlatform.jacocoOptions.taskGenerationEnabled = false

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
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)
}