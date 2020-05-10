buildscript {

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(GradlePlugin.androidToolsBuildGradle)
        classpath(GradlePlugin.kotlinGradlePlugin)
        classpath(GradlePlugin.navigationSafeArgsGradlePlugin)
        classpath(GradlePlugin.androidJunit5Plugin)
        classpath(GradlePlugin.googleServicePlugin)
        classpath(GradlePlugin.paperworkPlugin)
        classpath(GradlePlugin.koinGradlePlugin)
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.8.0")
}

allprojects {

    repositories {
        google()
        jcenter()
    }

    apply(from = "$rootDir/ktlint.gradle.kts")
    apply(from = "$rootDir/detekt.gradle")

    afterEvaluate {
        plugins.forEach { plugin ->
            (plugin as? com.android.build.gradle.internal.plugins.BasePlugin)?.let { libraryPlugin ->
                libraryPlugin.extension.apply {
                    compileSdkVersion(BuildVersions.compileSdkVersion)
                    buildToolsVersion = BuildVersions.buildToolsVersion

                    defaultConfig {
                        minSdkVersion(BuildVersions.minSdkVersion)
                        targetSdkVersion(BuildVersions.targetSdkVersion)
                        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    }

                    compileOptions {
                        coreLibraryDesugaringEnabled = true
                        sourceCompatibility = JavaVersion.VERSION_1_8
                        targetCompatibility = JavaVersion.VERSION_1_8
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

                    buildFeatures.viewBinding = true
                    lintOptions.isAbortOnError = false
                }
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
    }
}

tasks.register("clean").configure {
    delete("build")
}

apply(from = "$rootDir/emulator.gradle.kts")
apply(from = "$rootDir/ci.gradle.kts")

println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
