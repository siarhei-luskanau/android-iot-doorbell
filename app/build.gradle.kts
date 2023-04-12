plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "siarhei.luskanau.iot.doorbell"
    compileSdk = BuildVersions.compileSdkVersion
    buildToolsVersion = BuildVersions.buildToolsVersion

    defaultConfig {
        minSdk = BuildVersions.minSdkVersion
        targetSdk = BuildVersions.targetSdkVersion
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
        create("diSingleton") { dimension = "di_variant" }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
        kotlinCompilerExtensionVersion = PublicVersions.composeCompiler
    }
}

dependencies {
    implementation(project(":di:di"))
    "diDaggerImplementation"(project(":di:di_dagger:di_dagger"))
    "diKodeinImplementation"(project(":di:di_kodein"))
    "diKoinImplementation"(project(":di:di_koin:di_koin"))
    "diSingletonImplementation"(project(":di:di_singleton"))
    "diToothpickImplementation"(project(":di:di_toothpick"))

    implementation(project(":base_android"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_persistence"))
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

    implementation(Libraries.activityCompose)
    implementation(Libraries.activityKtx)
    implementation(Libraries.androidxStartup)
    implementation(Libraries.composeAnimation)
    implementation(Libraries.composeMaterial)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.material)
    implementation(Libraries.navigationFragmentKtx)
    implementation(Libraries.timber)
    implementation(Libraries.workRuntimeKtx)

    // Development
    debugImplementation(Libraries.leakCanary)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.androidTestExtTruth)
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoContrib)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.testEspressoIntents)
    androidTestImplementation(TestLibraries.testExtJunitKtx)
}

apply(plugin = "com.google.gms.google-services")
