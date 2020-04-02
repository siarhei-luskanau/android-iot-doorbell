plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    // id("de.mannodermaus.android-junit5")
    id("hu.supercluster.paperwork")
}

paperwork {
    set = mapOf(
        "gitSha" to gitSha(),
        "gitBranch" to gitBranch(),
        "buildDate" to buildTime("yyyy-MM-dd HH:mm:ss", "GMT")
    )
}

android {
    androidExtensions {
        features = setOf("parcelize")
    }

    testOptions {
        animationsDisabled = true
        unitTests(delegateClosureOf<com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions> {
            //isReturnDefaultValues = true
            all { test: Test ->
                test.testLogging.events = setOf(
                    org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                    org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                )
            }
        })
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