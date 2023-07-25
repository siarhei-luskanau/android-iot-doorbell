val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.android.library")
    id("shot")
    kotlin("multiplatform")
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.findVersion("build-jvmTarget").get().requiredVersion
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.findLibrary("androidx-activity-compose").get())
                implementation(libs.findLibrary("androidx-tracing").get())
                implementation(libs.findLibrary("coil").get())
                implementation(libs.findLibrary("coil-compose").get())
                implementation(libs.findLibrary("compose-material").get())
                implementation(libs.findLibrary("compose-ui-tooling").get())
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                implementation(libs.findLibrary("mockk").get())
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(libs.findLibrary("compose-ui-test-manifest").get())
            }
        }
    }
}

android {
    compileSdk = libs.findVersion("android-build-compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("android-build-minSdk").get().requiredVersion.toInt()
        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
        testApplicationId = "siarhei.luskanau.iot.doorbell.testapp"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = false
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf(
            libs.findVersion("build-javaVersion").get().requiredVersion,
        )
        targetCompatibility = JavaVersion.valueOf(
            libs.findVersion("build-javaVersion").get().requiredVersion,
        )
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
        kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().requiredVersion
    }

    packagingOptions.resources.excludes.apply {
        add("META-INF/AL2.0")
        add("META-INF/LGPL2.1")
        add("META-INF/LICENSE-notice.md")
        add("META-INF/LICENSE.md")
    }
}

dependencies {
    implementation(platform(libs.findLibrary("compose-bom").get()))
}

shot {
    tolerance = 0.1 // 0,1% tolerance
}
