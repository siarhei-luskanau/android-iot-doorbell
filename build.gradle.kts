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
        classpath(GradlePlugin.googleServicePlugin)
        classpath(GradlePlugin.paperworkPlugin)
        classpath(GradlePlugin.hiltGradlePlugin)
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version(PublicVersions.detekt)
    id("org.jetbrains.kotlinx.kover").version(PublicVersions.kotlinxKover)
}

apply(from = "$rootDir/emulator.gradle.kts")
apply(from = "$rootDir/ci.gradle.kts")

allprojects {

    repositories {
        google()
        mavenCentral()
    }

    apply(from = "$rootDir/ktlint.gradle.kts")
    apply(plugin = "io.gitlab.arturbosch.detekt")
}

tasks.register("clean").configure {
    delete("build")
}
