val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvmToolchain(libs.findVersion("javaVersion").get().requiredVersion.toInt())

    androidTarget()

    sourceSets {
        commonMain.dependencies {
                implementation(libs.findLibrary("jetbrains-compose-animation").get())
                implementation(libs.findLibrary("jetbrains-compose-animation-graphics").get())
                implementation(libs.findLibrary("jetbrains-compose-components-resources").get())
                implementation(libs.findLibrary("jetbrains-compose-foundation").get())
                implementation(libs.findLibrary("jetbrains-compose-material3").get())
                implementation(libs.findLibrary("jetbrains-compose-runtime").get())
                implementation(libs.findLibrary("jetbrains-compose-ui").get())
                implementation(libs.findLibrary("jetbrains-compose-ui-tooling-preview").get())
                implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-compose").get())
                implementation(libs.findLibrary("jetbrains-navigation-compose").get())
        }
    commonTest.dependencies {
        }
        androidMain.dependencies {
                implementation(libs.findLibrary("android-material").get())
                implementation(libs.findLibrary("androidx-activity-compose").get())
                implementation(libs.findLibrary("androidx-activity-ktx").get())
                implementation(libs.findLibrary("androidx-tracing").get())
                implementation(libs.findLibrary("coil-compose").get())
        }
        val androidUnitTest by getting {
            dependencies {
                //implementation(libs.findLibrary("androidx-paging-testing").get())
                implementation(libs.findLibrary("espresso-core").get())
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                implementation(libs.findLibrary("mockk").get())
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(libs.findLibrary("espresso-core").get())
                implementation(libs.findLibrary("kotlin-test").get())
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
        viewBinding = false
        buildConfig = false
        compose = true
    }

    testOptions.configureAndroidTestOptions()

    packaging.resources.excludes.add("META-INF/**")
}

tasks.withType<AbstractTestTask>().configureEach {
    failOnNoDiscoveredTests = false
}
