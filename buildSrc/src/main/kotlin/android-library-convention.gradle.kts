plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdk = BuildVersions.compileSdkVersion
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdk = BuildVersions.minSdkVersion
        targetSdk = BuildVersions.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        unitTests.all {
            it.testLogging.events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = PublicVersions.compose
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
    }
}