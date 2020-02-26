plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    // id("de.mannodermaus.android-junit5")
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

    buildFeatures {
        viewBinding = true
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
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.lifecycleLivedataKtx)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.coil)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCore)
    androidTestImplementation(TestLibraries.fragmentTesting)
    kaptAndroidTest(TestLibraries.databindingCompiler)
}
