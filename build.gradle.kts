println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
System.getProperties().forEach { key, value -> println("System.getProperties(): $key=$value") }
System.getenv().forEach { (key, value) -> println("System.getenv(): $key=$value") }

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.ktlint.jlleitschuh)
}

apply(from = "$rootDir/ci.gradle.kts")

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        version.set("0.50.0")
    }
}

kover {
    reports {
        verify {
            rule {
                minBound(95)
                maxBound(98)
            }
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
    it.dependencies { kover(project(it.path)) }
}

tasks.register("clean").configure {
    delete("build")
}
