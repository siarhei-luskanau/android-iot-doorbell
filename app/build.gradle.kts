plugins {
    id("com.android.application")
    kotlin("android")
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
        create("diToothpick") { dimension = "di_variant" }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = false
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.valueOf(libs.versions.build.javaVersion.get())
        targetCompatibility = JavaVersion.valueOf(libs.versions.build.javaVersion.get())
    }
    kotlinOptions {
        jvmTarget = libs.versions.build.jvmTarget.get()
    }

    testOptions {
        animationsDisabled = true
        unitTests.all {
            it.testLogging.events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":di:di"))
    "diDaggerImplementation"(project(":di:di_dagger:di_dagger"))
    "diKodeinImplementation"(project(":di:di_kodein"))
    "diKoinImplementation"(project(":di:di_koin:di_koin"))
    "diManualImplementation"(project(":di:di_manual"))
    "diToothpickImplementation"(project(":di:di_toothpick"))

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
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.compose.material)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.timber)

    // android test
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.junit.ktx)
    androidTestImplementation(libs.androidx.test.truth)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.intents)
    androidTestImplementation(libs.kotlin.test)
    androidTestImplementation(project(":common:common_test_ui"))
}

apply(plugin = "com.google.gms.google-services")
