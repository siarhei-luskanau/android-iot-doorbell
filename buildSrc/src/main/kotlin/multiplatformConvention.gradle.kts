@file:OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)

val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvmToolchain(21)

    androidLibrary {
        compileSdk = libs.findVersion("android-build-compileSdk").get().requiredVersion.toInt()
        minSdk = libs.findVersion("android-build-minSdk").get().requiredVersion.toInt()

        androidResources {
            enable = true
        }

        withHostTest {
            isIncludeAndroidResources = true
        }

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            animationsDisabled = true
        }

        packaging.resources.excludes.apply {
            add("META-INF/AL2.0")
            add("META-INF/LGPL2.1")
            add("META-INF/LICENSE-notice.md")
            add("META-INF/LICENSE.md")
            add("META-INF/com.google.dagger_dagger.version")
        }
    }

    sourceSets {
        commonMain.dependencies {
        }
        commonTest.dependencies {
        }
        androidMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(libs.findLibrary("android-material").get())
            implementation(libs.findLibrary("androidx-activity-compose").get())
            implementation(libs.findLibrary("androidx-activity-ktx").get())
            implementation(libs.findLibrary("androidx-tracing").get())
            implementation(libs.findLibrary("coil-compose").get())
            implementation(compose.material)
            implementation(compose.uiTooling)
            implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-compose").get())
            implementation(libs.findLibrary("jetbrains-navigation-compose").get())
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.findLibrary("androidx-paging-testing").get())
            implementation(libs.findLibrary("espresso-core").get())
            implementation(libs.findLibrary("kotlin-test").get())
            implementation(libs.findLibrary("kotlinx-coroutines-test").get())
            implementation(libs.findLibrary("mockk").get())
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.findLibrary("espresso-core").get())
            implementation(libs.findLibrary("kotlin-test").get())
            implementation(compose.uiTest)
        }
    }
}

dependencies {
    "androidMainImplementation"(platform(libs.findLibrary("firebase-bom").get()))
}

tasks.withType<AbstractTestTask>().configureEach {
    failOnNoDiscoveredTests = false
}
