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
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
                    )
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                }
            }
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = PublicVersions.composeCompiler
    }

    packagingOptions.resources.excludes.apply {
        add("META-INF/AL2.0")
        add("META-INF/LGPL2.1")
        add("META-INF/LICENSE.md")
        add("META-INF/LICENSE-notice.md")
    }

    dependencies {
        "coreLibraryDesugaring"(Libraries.desugarJdkLibs)
        "implementation"(Libraries.activityCompose)
        "implementation"(platform(Libraries.composeBom))
        "implementation"(Libraries.composeMaterial)
        "implementation"(Libraries.composeAnimation)
        "implementation"(Libraries.composeUiTooling)
        "implementation"(Libraries.composeMaterialTheme)
        "implementation"(Libraries.coil)
        "implementation"(Libraries.coilCompose)
        "androidTestImplementation"(platform(Libraries.composeBom))
        "androidTestImplementation"(TestLibraries.composeUiTestJunit4)
        "androidTestImplementation"(TestLibraries.composeUiTestManifest)
    }
}

shot {
    tolerance =  0.1 // 0,1% tolerance
}
