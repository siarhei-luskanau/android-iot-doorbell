plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-android-extensions")
    id("de.mannodermaus.android-junit5")
    id("hu.supercluster.paperwork")
}

paperwork {
    set = mapOf(
        "gitSha" to gitSha(),
        "gitBranch" to gitBranch(),
        "buildDate" to buildTime("yyyy-MM-dd HH:mm:ss", "GMT")
    )
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"].toString().toInt())
    buildToolsVersion = rootProject.extra["buildToolsVersion"].toString()

    defaultConfig {
        minSdkVersion(rootProject.extra["minSdkVersion"].toString().toInt())
        targetSdkVersion(rootProject.extra["targetSdkVersion"].toString().toInt())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    androidExtensions {
        features = setOf("parcelize")
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
    compileOnly("com.google.android.things:androidthings:${rootProject.extra["androidthingsVersion"]}")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${rootProject.extra["kotlinxCoroutinesVersion"]}")
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")

    implementation("androidx.paging:paging-runtime-ktx:${rootProject.extra["pagingVersion"]}")
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["fragmentVersion"]}")

    implementation("io.reactivex.rxjava2:rxjava:${rootProject.extra["rxJavaVersion"]}")
    implementation("io.reactivex.rxjava2:rxkotlin:${rootProject.extra["rxKotlinVersion"]}")
    implementation("io.reactivex.rxjava2:rxandroid:${rootProject.extra["rxAndroidVersion"]}")

    implementation("hu.supercluster:paperwork:${rootProject.extra["paperworkVersion"]}")

    //unit test
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:${rootProject.extra["spekVersion"]}")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:${rootProject.extra["spekVersion"]}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlinVersion"]}")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:${rootProject.extra["mockitoKotlinVersion"]}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["kotlinxCoroutinesVersion"]}")

    //android test
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test:${rootProject.extra["kotlinVersion"]}")
    androidTestImplementation("androidx.test.espresso:espresso-core:${rootProject.extra["espressoVersion"]}")
    androidTestImplementation("androidx.test:core:${rootProject.extra["androidTestCoreVersion"]}")
}