plugins {
    id("com.android.application")
    kotlin("plugin.compose")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell"
    compileSdk = libs.versions.android.build.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.build.minSdk.get().toInt()
        targetSdk = libs.versions.android.build.targetSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        applicationId = "siarhei.luskanau.iot.doorbell"
        versionCode = 1
        versionName = "1.0"
    }

    flavorDimensions += "di_variant"
    productFlavors {
        create("diDagger") { dimension = "di_variant" }
        create("diKodein") { dimension = "di_variant" }
        create("diKoin") { dimension = "di_variant" }
        create("diManual") { dimension = "di_variant" }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all { test: Test ->
                test.testLogging {
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    events = org.gradle.api.tasks.testing.logging.TestLogEvent.entries.toSet()
                }
            }
        }
        animationsDisabled = true
        libs.versions.android.emulators.get().split(",").map { it.toInt() }.forEach { version ->
            managedDevices.localDevices.create("managedVirtualDevice$version") {
                device = "Pixel 2"
                apiLevel = version
            }
        }
    }
}

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())
}

dependencies {
    implementation(project(":di:di"))
    "diDaggerImplementation"(project(":di:di_dagger:di_dagger"))
    "diKodeinImplementation"(project(":di:di_kodein"))
    "diKoinImplementation"(project(":di:di_koin:di_koin"))
    "diManualImplementation"(project(":di:di_manual"))

    implementation(libs.jetbrains.navigation.compose)
    implementation(project(":base_android"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_work_manager"))
    implementation(project(":common:common"))
    implementation(project(":data:dataDoorbellApi"))
    implementation(project(":data:dataDoorbellApiFirebase"))
    implementation(project(":data:dataDoorbellApiStub"))
    implementation(project(":navigation"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":ui:ui_splash"))

    debugImplementation(libs.leakcanary.android)
    implementation(libs.android.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.jetbrains.compose.material3)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.timber)

    // unit test
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.test.junit.ktx)
    testImplementation(libs.androidx.test.truth)
    testImplementation(libs.espresso.contrib)
    testImplementation(libs.espresso.core)
    testImplementation(libs.espresso.intents)
    testImplementation(libs.kotlin.test)
    testImplementation(project(":common:common_test_ui"))
}

apply(plugin = "com.google.gms.google-services")
