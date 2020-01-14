plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("de.mannodermaus.android-junit5")
}

android {
    compileSdkVersion(rootProject.extra["compileSdkVersion"].toString().toInt())
    buildToolsVersion = rootProject.extra["buildToolsVersion"].toString()

    defaultConfig {
        minSdkVersion(rootProject.extra["minSdkVersion"].toString().toInt())
        targetSdkVersion(rootProject.extra["targetSdkVersion"].toString().toInt())
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    viewBinding {
        isEnabled = true
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${rootProject.extra["kotlinVersion"]}")
    implementation("com.jakewharton.timber:timber:${rootProject.extra["timberVersion"]}")

    implementation("com.google.android.material:material:${rootProject.extra["materialVersion"]}")
    implementation("androidx.constraintlayout:constraintlayout:${rootProject.extra["constraintLayoutVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.paging:paging-runtime-ktx:${rootProject.extra["pagingVersion"]}")
    implementation("androidx.paging:paging-rxjava2-ktx:${rootProject.extra["pagingVersion"]}")
    implementation("io.coil-kt:coil:${rootProject.extra["coilVersion"]}")

    implementation("io.reactivex.rxjava2:rxjava:${rootProject.extra["rxJavaVersion"]}")
    implementation("io.reactivex.rxjava2:rxkotlin:${rootProject.extra["rxKotlinVersion"]}")
}
