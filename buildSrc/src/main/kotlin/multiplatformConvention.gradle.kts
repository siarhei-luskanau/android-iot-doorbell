val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvmToolchain(libs.findVersion("javaVersion").get().requiredVersion.toInt())

    androidLibrary {
        compileSdk = libs.findVersion("android-build-compileSdk").get().requiredVersion.toInt()
        minSdk = libs.findVersion("android-build-minSdk").get().requiredVersion.toInt()

        androidResources.enable = true

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            animationsDisabled = true
            EMULATOR_VERSIONS.forEach { version ->
                managedDevices.localDevices.create("managedVirtualDevice$version") {
                    device = "Pixel 2"
                    apiLevel = version
                }
            }
        }

        packaging.resources.excludes.add("META-INF/**")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.findLibrary("jetbrains-compose-animation").get())
            implementation(libs.findLibrary("jetbrains-compose-animation-graphics").get())
            implementation(libs.findLibrary("jetbrains-compose-components-resources").get())
            implementation(libs.findLibrary("jetbrains-compose-foundation").get())
            implementation(libs.findLibrary("jetbrains-compose-material3").get())
            implementation(libs.findLibrary("jetbrains-compose-runtime").get())
            implementation(libs.findLibrary("jetbrains-compose-ui").get())
            implementation(libs.findLibrary("jetbrains-compose-ui-tooling-preview").get())
            implementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-compose").get())
            implementation(libs.findLibrary("jetbrains-navigation-compose").get())
        }
        commonTest.dependencies {
        }
        androidMain.dependencies {
            implementation(libs.findLibrary("android-material").get())
            implementation(libs.findLibrary("androidx-activity-compose").get())
            implementation(libs.findLibrary("androidx-activity-ktx").get())
            implementation(libs.findLibrary("androidx-tracing").get())
            implementation(libs.findLibrary("coil-compose").get())
            implementation(project.dependencies.platform(libs.findLibrary("firebase-bom").get()))
        }
        val androidHostTest by getting {
            dependencies {
                implementation(libs.findLibrary("espresso-core").get())
                implementation(libs.findLibrary("kotlin-test").get())
                implementation(libs.findLibrary("kotlinx-coroutines-test").get())
                implementation(libs.findLibrary("mockk").get())
            }
        }
        val androidDeviceTest by getting {
            dependencies {
                implementation(libs.findLibrary("compose-ui-test-junit4").get())
                implementation(libs.findLibrary("espresso-core").get())
                implementation(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    testLogging {
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events = org.gradle.api.tasks.testing.logging.TestLogEvent.entries.toSet()
    }
}

tasks.withType<AbstractTestTask>().configureEach {
    failOnNoDiscoveredTests = false
}

// KSP generates Java code (e.g. Dagger components) which the KMP library plugin
// cannot compile (no Java compilation task). Register a JavaCompile task to handle it.
pluginManager.withPlugin("com.google.devtools.ksp") {
    afterEvaluate {
        val kspJavaDir = layout.buildDirectory.dir("generated/ksp/android/androidMain/java")
        if (kspJavaDir.get().asFile.exists() || tasks.findByName("kspAndroidMain") != null) {
            val compileAndroidMain =
                tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileAndroidMain")
            val compileKspJava = tasks.register<JavaCompile>("compileKspGeneratedJava") {
                dependsOn("kspAndroidMain", compileAndroidMain)
                source(fileTree(kspJavaDir))
                classpath = files(compileAndroidMain.map { it.outputs.files }) +
                        compileAndroidMain.get().libraries
                destinationDirectory.set(
                    layout.buildDirectory.dir("classes/kotlin/android/main")
                )
                sourceCompatibility =
                    libs.findVersion("javaVersion").get().requiredVersion
                targetCompatibility =
                    libs.findVersion("javaVersion").get().requiredVersion
            }
            tasks.named("bundleAndroidMainClassesToCompileJar") { dependsOn(compileKspJava) }
            tasks.named("bundleAndroidMainClassesToRuntimeJar") { dependsOn(compileKspJava) }
            tasks.named("bundleLibRuntimeToDirAndroidMain") { dependsOn(compileKspJava) }
            tasks.findByName("processAndroidMainJavaRes")?.dependsOn(compileKspJava)
        }
    }
}
