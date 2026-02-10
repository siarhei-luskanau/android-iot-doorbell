import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("com.android.kotlin.multiplatform.library")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    kotlin("plugin.compose")
}

kotlin {
    jvmToolchain(libs.versions.javaVersion.get().toInt())

    androidLibrary {
        compileSdk = libs.versions.android.build.compileSdk.get().toInt()
        minSdk = libs.versions.android.build.minSdk.get().toInt()

        androidResources.enable = true

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            animationsDisabled = true
            libs.versions.android.emulators.get().split(",").map { it.toInt() }.forEach { version ->
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
            implementation(libs.jetbrains.compose.animation)
            implementation(libs.jetbrains.compose.animation.graphics)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.ui.tooling.preview)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.navigation.compose)
        }
        commonTest.dependencies {
        }
        androidMain.dependencies {
            implementation(libs.android.material)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.activity.ktx)
            implementation(libs.androidx.tracing)
            implementation(libs.coil.compose)
            implementation(project.dependencies.platform(libs.firebase.bom))
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.espresso.core)
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.mockk)
        }
        getByName("androidDeviceTest").dependencies {
            implementation(libs.compose.ui.test.junit4)
            implementation(libs.espresso.core)
            implementation(libs.kotlin.test)
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
                sourceCompatibility = libs.versions.javaVersion.get()
                targetCompatibility = libs.versions.javaVersion.get()
            }
            tasks.named("bundleAndroidMainClassesToCompileJar") { dependsOn(compileKspJava) }
            tasks.named("bundleAndroidMainClassesToRuntimeJar") { dependsOn(compileKspJava) }
            tasks.named("bundleLibRuntimeToDirAndroidMain") { dependsOn(compileKspJava) }
            tasks.findByName("processAndroidMainJavaRes")?.dependsOn(compileKspJava)
        }
    }
}
