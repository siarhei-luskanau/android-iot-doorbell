println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
System.getProperties().forEach { key, value -> println("System.getProperties(): $key=$value") }
System.getenv().forEach { (key, value) -> println("System.getenv(): $key=$value") }

buildscript {
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.androidx.navigation.safeArgsGradlePlugin)
        classpath(libs.google.services)
        classpath(libs.karumiShot)
        classpath(libs.kotlin.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinx.kover)
}

apply(from = "$rootDir/ci.gradle.kts")

allprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(from = "$rootDir/ktlint.gradle.kts")
}

dependencies {
    subprojects.map { it.path }.forEach {
        kover(project(it))
    }
}

tasks.register("clean").configure {
    delete("build")
}
