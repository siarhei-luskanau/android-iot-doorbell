plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
    id("com.google.gms.google-services")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"].toString().toInt())
    buildToolsVersion = rootProject.extra["buildToolsVersion"].toString()

    defaultConfig {
        applicationId = "siarhei.luskanau.iot.doorbell"
        minSdkVersion(rootProject.extra["minSdkVersion"].toString().toInt())
        targetSdkVersion(rootProject.extra["targetSdkVersion"].toString().toInt())
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    viewBinding {
        isEnabled = true
    }

    lintOptions {
        isAbortOnError = false
    }

    testOptions {
        animationsDisabled = true
        unitTests(delegateClosureOf<com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions> {
            //isReturnDefaultValues = true
            all(KotlinClosure1<Any, Test>({
                (this as Test).also { testTask ->
                    testTask.testLogging.events = setOf(
                        org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                        org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                    )
                }
            }, this))
        })
    }
}

dependencies {
    implementation(project(":common:common"))
    implementation(project(":base_camera"))
    implementation(project(":base_file"))
    implementation(project(":base_firebase"))
    implementation(project(":base_persistence"))
    implementation(project(":base_cache"))
    implementation(project(":base_work_manager"))
    implementation(project(":ui:ui_common"))
    implementation(project(":ui:ui_permissions"))
    implementation(project(":ui:ui_doorbell_list"))
    implementation(project(":ui:ui_image_list"))
    implementation(project(":ui:ui_image_details"))
    implementation(project(":navigation"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")

    // toothpick
    implementation("com.github.stephanenicolas.toothpick:ktp:${rootProject.extra["toothpickVersion"]}")
    kapt("com.github.stephanenicolas.toothpick:toothpick-compiler:${rootProject.extra["toothpickVersion"]}")

    implementation("io.reactivex.rxjava2:rxkotlin:${rootProject.extra["rxKotlinVersion"]}")
    implementation("io.reactivex.rxjava2:rxandroid:${rootProject.extra["rxAndroidVersion"]}")

    implementation("com.google.android.material:material:${rootProject.extra["materialVersion"]}")
    kapt("androidx.lifecycle:lifecycle-common-java8:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-extensions:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-reactivestreams-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigationVersion"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["navigationVersion"]}")
    implementation("androidx.paging:paging-runtime-ktx:${rootProject.extra["pagingVersion"]}")
    implementation("androidx.constraintlayout:constraintlayout:${rootProject.extra["constraintLayoutVersion"]}")

    compileOnly("com.google.android.things:androidthings:${rootProject.extra["androidthingsVersion"]}")

    implementation("androidx.work:work-runtime-ktx:${rootProject.extra["workManagerVersion"]}")

    //Development
    debugImplementation("com.squareup.leakcanary:leakcanary-android:${rootProject.extra["leakCanaryVersion"]}")

    //unit test
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:${rootProject.extra["spekVersion"]}")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:${rootProject.extra["spekVersion"]}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlinVersion"]}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${rootProject.extra["mockitoKotlinVersion"]}")

    //android test
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlinVersion"]}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${rootProject.extra["espressoVersion"]}")
    androidTestImplementation("androidx.test.espresso:espresso-intents:${rootProject.extra["espressoVersion"]}")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:${rootProject.extra["espressoVersion"]}")
    androidTestImplementation("androidx.test:core:${rootProject.extra["androidTestCoreVersion"]}")
}