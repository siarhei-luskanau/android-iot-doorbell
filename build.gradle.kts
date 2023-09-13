println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
System.getProperties().forEach { key, value -> println("System.getProperties(): $key=$value") }
System.getenv().forEach { (key, value) -> println("System.getenv(): $key=$value") }

buildscript {
    dependencies {
        classpath(libs.androidx.navigation.safeArgsGradlePlugin)
        classpath(libs.google.services)
    }
}

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.ktlint.jlleitschuh)
}

apply(from = "$rootDir/ci.gradle.kts")

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    ktlint {
        version.set("0.50.0")
    }
}

koverReport {
    verify {
        rule {
            minBound(95)
            maxBound(98)
        }
    }
}

subprojects.filter {
    !listOf(
        ":",
        ":app",
        ":common",
        ":data",
        ":di",
        ":ui",
        ":di:di_dagger",
        ":di:di_koin",
    ).contains(it.path)
}.forEach {
    it.apply(plugin = "org.jetbrains.kotlinx.kover")
    it.koverReport {
        defaults {
            mergeWith("debug")
        }
    }
    it.dependencies { kover(project(it.path)) }
}

tasks.register("clean").configure {
    delete("build")
}
