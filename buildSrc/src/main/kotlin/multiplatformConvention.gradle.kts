val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.android.library")
    id("io.github.takahirom.roborazzi")
    kotlin("multiplatform")
    kotlin("plugin.compose")
}

kotlin {
    androidTarget {
        compilations.configureEach {
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
                implementation(libs.findLibrary("android-material").get())
                implementation(libs.findLibrary("androidx-activity-compose").get())
                implementation(libs.findLibrary("androidx-activity-ktx").get())
                implementation(libs.findLibrary("androidx-fragment-ktx").get())
                implementation(libs.findLibrary("androidx-tracing").get())
                implementation(libs.findLibrary("coil").get())
                implementation(libs.findLibrary("coil-compose").get())
                implementation(libs.findLibrary("compose-material").get())
                implementation(libs.findLibrary("compose-ui-tooling").get())
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.findLibrary("androidx-fragment-testing").get())
                implementation(libs.findLibrary("androidx-paging-testing").get())
                implementation(libs.findLibrary("espresso-core").get())
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                implementation(libs.findLibrary("mockk").get())
                implementation(libs.findLibrary("robolectric").get())
                implementation(libs.findLibrary("roborazzi").get())
                implementation(libs.findLibrary("roborazzi-compose").get())
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.findLibrary("espresso-core").get())
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
            }
        }
    }
}

android {
    compileSdk = libs.findVersion("android-build-compileSdk").get().requiredVersion.toInt()

    defaultConfig {
        minSdk = libs.findVersion("android-build-minSdk").get().requiredVersion.toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    testOptions.configureAndroidTestOptions()

    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("compose-compiler").get().requiredVersion
    }

    packagingOptions.resources.excludes.apply {
        add("META-INF/AL2.0")
        add("META-INF/LGPL2.1")
        add("META-INF/LICENSE-notice.md")
        add("META-INF/LICENSE.md")
        add("META-INF/com.google.dagger_dagger.version")
    }
}

dependencies {
    implementation(platform(libs.findLibrary("compose-bom").get()))
}
