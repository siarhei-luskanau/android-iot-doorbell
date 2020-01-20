plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
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

    viewBinding {
        isEnabled = true
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
    implementation(project(":common:common"))
    implementation(project(":ui:ui_common"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    implementation(Libraries.material)
    implementation(Libraries.swiperefreshlayout)
    implementation(Libraries.constraintLayout)
    implementation(Libraries.lifecycleRuntimeKtx)
    implementation(Libraries.lifecycleViewmodelKtx)
    implementation(Libraries.pagingRuntimeKtx)
    implementation(Libraries.pagingRxjava2Ktx)
    implementation(Libraries.navigationUiKtx)
    implementation(Libraries.coil)

    implementation(Libraries.rxJava)
    implementation(Libraries.rxKotlin)

    // unit test
    testImplementation(project(":common:common_test"))
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)
    testImplementation(TestLibraries.kotlinxCoroutinesTest)

    // android test
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.androidTestCore)
    androidTestImplementation(TestLibraries.fragmentTesting)
    kaptAndroidTest(TestLibraries.databindingCompiler)
}
