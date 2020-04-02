plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    // id("de.mannodermaus.android-junit5")
    id("com.google.gms.google-services")
}

android {
    defaultConfig {
        applicationId = "siarhei.luskanau.iot.doorbell"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
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
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))
    implementation(project(":base_android"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_firebase"))
    implementation(project(":base_persistence"))
    implementation(project(":base_cache"))
    implementation(project(":base_work_manager"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":navigation"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinReflect)
    implementation(Libraries.timber)

    kapt(Libraries.lifecycleCommonJava8)
    implementation(Libraries.navigationFragmentKtx)
    implementation(Libraries.pagingCommonKtx)
    implementation(Libraries.pagingRuntimeKtx)

    implementation(Libraries.workRuntimeKtx)

    // kodein
    implementation(Libraries.kodeinDiGenericJvm)
    implementation(Libraries.kodeinDiFrameworkAndroidX)

    // Development
    debugImplementation(Libraries.leakCanary)

    // Android Things
    compileOnly(Libraries.androidthings)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.testEspressoIntents)
    androidTestImplementation(TestLibraries.testEspressoContrib)
    androidTestImplementation(TestLibraries.androidTestCore)
    androidTestImplementation(TestLibraries.androidTestExtTruth)
    androidTestImplementation(TestLibraries.testExtJunit)
}