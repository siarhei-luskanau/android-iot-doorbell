println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
System.getProperties().forEach { key, value -> println("System.getProperties(): $key=$value") }
System.getenv().forEach { (key, value) -> println("System.getenv(): $key=$value") }

buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.google.services)
        classpath(libs.karumiShot)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.navigation.safeArgsGradlePlugin)
        classpath(libs.paperwork.plugin)
    }
}

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinx.kover)
}

apply(from = "$rootDir/ci.gradle.kts")

allprojects {

    repositories {
        google()
        mavenCentral()
    }

    apply(plugin = "kover")
    apply(from = "$rootDir/ktlint.gradle.kts")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    koverMerged.enable()
}

tasks.register("clean").configure {
    delete("build")
}
