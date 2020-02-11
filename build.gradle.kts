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
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.5.0")
}

allprojects {

    repositories {
        google()
        jcenter()
    }

    apply(from = "$rootDir/ktlint.gradle.kts")
    apply(from = "$rootDir/detekt.gradle")

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions { jvmTarget = JavaVersion.VERSION_1_8.toString() }
    }
}

tasks.register("clean").configure {
    delete("build")
}

apply(from = "$rootDir/ci.gradle.kts")

println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
