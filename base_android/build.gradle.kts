plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    // id("de.mannodermaus.android-junit5")
    id("hu.supercluster.paperwork")
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

android {
    androidExtensions {
        features = setOf("parcelize")
    }
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)

    implementation(Libraries.paperwork)

    // Android Things
    compileOnly(Libraries.androidthings)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)
}