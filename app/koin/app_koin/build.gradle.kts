import de.mannodermaus.gradle.plugins.junit5.junitPlatform

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
    id("com.google.gms.google-services")
    id("koin")
}

android.testOptions.junitPlatform.jacocoOptions.taskGenerationEnabled = false

android {
    defaultConfig {
        applicationId = "siarhei.luskanau.iot.doorbell"
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    coreLibraryDesugaring(Libraries.desugarJdkLibs)

    implementation(project(":common:common"))
    implementation(project(":base_android"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_firebase"))
    implementation(project(":base_persistence"))
    implementation(project(":base_cache"))
    implementation(project(":base_work_manager"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_splash"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":navigation"))
    implementation(project(":app:koin:koin_common"))
    implementation(project(":app:koin:koin_doorbell_list"))
    implementation(project(":app:koin:koin_image_details"))
    implementation(project(":app:koin:koin_image_list"))
    implementation(project(":app:koin:koin_permissions"))
    implementation(project(":app:koin:koin_splash"))

    implementation(Libraries.kotlinStdlibJdk8)
    implementation(Libraries.kotlinxCoroutinesCore)
    implementation(Libraries.timber)

    kapt(Libraries.lifecycleCommonJava8)
    implementation(Libraries.material)
    implementation(Libraries.activityKtx)
    implementation(Libraries.fragmentKtx)
    implementation(Libraries.navigationFragmentKtx)

    implementation(Libraries.workRuntimeKtx)

    // koin
    implementation(Libraries.koinAndroid)

    // Development
    debugImplementation(Libraries.leakCanary)

    // Android Things
    compileOnly(Libraries.androidthings)

    // unit test
    testRuntimeOnly(TestLibraries.spekRunnerJunit5)
    testImplementation(TestLibraries.spekDslJvm)
    testImplementation(TestLibraries.kotlinTest)
    testImplementation(TestLibraries.mockitoKotlin)

    // android test
    androidTestImplementation(project(":common:common_test_ui"))
    androidTestImplementation(TestLibraries.kotlinTest)
    androidTestImplementation(TestLibraries.testEspressoCore)
    androidTestImplementation(TestLibraries.testEspressoIntents)
    androidTestImplementation(TestLibraries.testEspressoContrib)
    androidTestImplementation(TestLibraries.androidTestCoreKtx)
    androidTestImplementation(TestLibraries.androidTestExtTruth)
    androidTestImplementation(TestLibraries.testExtJunitKtx)
}