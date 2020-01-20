plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("de.mannodermaus.android-junit5")
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
    compileSdkVersion(BuildVersions.compileSdkVersion)
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdkVersion(BuildVersions.minSdkVersion)
        targetSdkVersion(BuildVersions.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    androidExtensions {
        features = setOf("parcelize")
    }

    testOptions {
        animationsDisabled = true
        unitTests(delegateClosureOf<com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions> {
            //isReturnDefaultValues = true
            all(KotlinClosure1<Any, Test>({
                (this as Test).also { testTask ->
                    testTask.testLogging.events = setOf(
                        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                    )
                }
            }, this))
        })
    }
}

dependencies {
    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.fragmentKtx)

    implementation(Libraries.rxJava)
    implementation(Libraries.rxKotlin)
    implementation(Libraries.rxAndroid)

    implementation(Libraries.paperwork)

    // Android Things
    compileOnly(Libraries.androidthings)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)

    // android test
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCore)
}