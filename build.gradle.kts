println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(GradlePlugin.androidToolsBuildGradle)
        classpath(GradlePlugin.kotlinGradlePlugin)
        classpath(GradlePlugin.navigationSafeArgsGradlePlugin)
        classpath(GradlePlugin.androidJunit5Plugin)
        classpath(GradlePlugin.googleServicePlugin)
        classpath(GradlePlugin.paperworkPlugin)
        classpath(GradlePlugin.koinGradlePlugin)
        classpath(GradlePlugin.hiltGradlePlugin)
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version(PublicVersions.detekt)
    id("com.vanniktech.android.junit.jacoco").version(PublicVersions.androidJunitJacoco)
}

apply(from = "$rootDir/emulator.gradle.kts")
apply(from = "$rootDir/ci.gradle.kts")

allprojects {

    repositories {
        google()
        jcenter()
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }

    apply(from = "$rootDir/ktlint.gradle.kts")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "jacoco")

    plugins.configureEach {
        (this as? com.android.build.gradle.internal.plugins.BasePlugin<*, *, *>)?.extension?.apply {
            compileSdkVersion(BuildVersions.compileSdkVersion)
            buildToolsVersion = BuildVersions.buildToolsVersion

            defaultConfig {
                minSdkVersion(BuildVersions.minSdkVersion)
                targetSdkVersion(BuildVersions.targetSdkVersion)
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildFeatures.viewBinding = true
            buildFeatures.buildConfig = false
            buildFeatures.compose = true

            compileOptions {
                isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            composeOptions {
                kotlinCompilerExtensionVersion = PublicVersions.compose
            }

            testOptions {
                animationsDisabled = true
                unitTests(delegateClosureOf<com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions> {
                    //isReturnDefaultValues = true
                    all { test: Test ->
                        test.testLogging.events = setOf(
                            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
                        )
                    }
                })
            }

            dependencies {
                "coreLibraryDesugaring"(Libraries.desugarJdkLibs)
                "implementation"(Libraries.composeRuntime)
            }
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
            freeCompilerArgs += listOf("-Xallow-jvm-ir-dependencies", "-Xskip-prerelease-check")
        }
    }
}

tasks.register("clean").configure {
    delete("build")
}

junitJacoco {
    jacocoVersion = PublicVersions.jacoco
    excludes = excludes.orEmpty().toMutableList().apply {
        add("**/siarhei/luskanau/iot/doorbell/common/test/*")
        add("**/siarhei/luskanau/iot/doorbell/common/test/ui/*")
        add("**/di/*")

        add("**/*Application*")
        add("**/*Activity.*")
        add("**/*Fragment.*")
        add("**/*Adapter.*")
        add("**/*ViewHolder.*")
        add("**/*Directions*.*")
        add("**/*Args*.*")
        add("**/*_Impl*.*")

        add("**/androidx/*")
        add("**/databinding/*")

        add("**/*\$*\$*.*")

        addAll(
            listOf(
                "**/R.class",
                "**/R2.class", // ButterKnife Gradle Plugin.
                "**/R$*.class",
                "**/R2$*.class", // ButterKnife Gradle Plugin.
                "**/*$$*",
                "**/*\$ViewInjector*.*", // Older ButterKnife Versions.
                "**/*\$ViewBinder*.*", // Older ButterKnife Versions.
                "**/*_ViewBinding*.*", // Newer ButterKnife Versions.
                "**/BuildConfig.*",
                "**/Manifest*.*",
                "**/*\$Lambda$*.*", // Jacoco can not handle several "$" in class name.
                "**/*Dagger*.*", // Dagger auto-generated code.
                "**/*MembersInjector*.*", // Dagger auto-generated code.
                "**/*_Provide*Factory*.*", // Dagger auto-generated code.
                "**/*_Factory*.*", // Dagger auto-generated code.
                "**/*\$JsonObjectMapper.*", // LoganSquare auto-generated code.
                "**/*\$inlined$*.*", // Kotlin specific, Jacoco can not handle several "$" in class name.
                "**/*\$Icepick.*", // Icepick auto-generated code.
                "**/*\$StateSaver.*", // android-state auto-generated code.
                "**/*AutoValue_*.*" // AutoValue auto-generated code.
            )
        )
    }
}