plugins {
    id("com.android.library")
    kotlin("android")
    id("shot")
}

android {
    compileSdk = BuildVersions.compileSdkVersion
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdk = BuildVersions.minSdkVersion
        targetSdk = BuildVersions.targetSdkVersion
        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
        testApplicationId = "siarhei.luskanau.iot.doorbell.testapp"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = false
        compose = true
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.testLogging {
                    events = setOf(
                        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                    )
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                }
            }
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = PublicVersions.compose
    }

    packagingOptions.excludes.apply {
        add("META-INF/AL2.0")
        add("META-INF/LGPL2.1")
    }

    dependencies {
        "coreLibraryDesugaring"(Libraries.desugarJdkLibs)
        // Integration with activities
        "implementation"(Libraries.activityCompose)
        // Compose Material Design
        "implementation"(Libraries.composeMaterial)
        // Animations
        "implementation"(Libraries.composeAnimation)
        // Tooling support (Previews, etc.)
        "implementation"(Libraries.composeUiTooling)
        // When using a MDC theme
        "implementation"(Libraries.composeMaterialTheme)
        "implementation"(Libraries.coil)
        "implementation"(Libraries.coilCompose)
        // Test rules and transitive dependencies
        "androidTestImplementation"(TestLibraries.composeUiTestJunit4)
        // Needed for createComposeRule, but not createAndroidComposeRule:
        "androidTestImplementation"(TestLibraries.composeUiTestManifest)
    }
}

shot {
    tolerance =  0.1 // 0,1% tolerance
}
