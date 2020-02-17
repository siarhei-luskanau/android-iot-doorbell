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
    id("io.gitlab.arturbosch.detekt").version("1.7.3")
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
            // println("plugin: $plugin")
            (plugin as? com.android.build.gradle.internal.plugins.BasePlugin<*,*>)?.let { libraryPlugin ->
                // println("LibraryPlugin: $libraryPlugin")

                libraryPlugin.extension.compileOptions {
                    isCoreLibraryDesugaringEnabled = true
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                libraryPlugin.extension.defaultConfig {
                    multiDexEnabled = true
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

apply(from = "$rootDir/ci.gradle.kts")

println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
