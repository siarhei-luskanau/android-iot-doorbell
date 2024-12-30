import org.apache.tools.ant.taskdefs.condition.Os
import java.io.File
import java.io.InputStream
import java.util.Properties

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

val CI_GRADLE = "CI_GRADLE"

tasks.register("ciLint") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "ktlintCheck",
            "detekt",
            "lintDebug",
        )
    }
}

tasks.register("ciUnitTest") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
//            "koverXmlReportDebug",
//            "koverXmlReport",
//            "koverHtmlReportDebug",
//            "koverHtmlReport",
//            "koverVerifyDebug",
//            "koverVerify",
        )
    }
}

tasks.register("ciRecordScreenshots") {
    group = CI_GRADLE
    doLast {
        gradlew("updateDebugScreenshotTest")
    }
}

tasks.register("ciBuildApp") {
    group = CI_GRADLE
    doLast {
        gradlew("assembleDebug")
        copy {
            from(rootProject.subprojects.map { it.layout.buildDirectory.asFile.get() })
            include("**/*.apk")
            exclude("**/apk/androidTest/**")
            eachFile { path = name }
            includeEmptyDirs = false
            into("${layout.buildDirectory.asFile.get().path}/apk/")
        }
    }
}

tasks.register("ciEmulatorJobsMatrixSetup") {
    group = CI_GRADLE
    doLast {
        EmulatorJobsMatrix().createMatrixJsonFile(rootProject = rootProject)
    }
}

tasks.register("ciSdkManagerLicenses") {
    group = CI_GRADLE
    doLast {
        val sdkDirPath = getAndroidSdkPath(rootDir = rootDir)
        getSdkmanagerFile(rootDir = rootDir)?.let { sdkmanagerFile ->
            val yesInputStream = object : InputStream() {
                private val yesString = "y\n"
                private var counter = 0
                override fun read(): Int = yesString[counter % 2].also { counter++ }.code
            }
            providers.exec {
                executable = sdkmanagerFile.absolutePath
                args = listOf("--list", "--sdk_root=$sdkDirPath")
                println("exec: ${this.commandLine.joinToString(separator = " ")}")
            }.apply { println("ExecResult: ${this.result.get()}") }
            @Suppress("DEPRECATION")
            exec {
                executable = sdkmanagerFile.absolutePath
                args = listOf("--licenses", "--sdk_root=$sdkDirPath")
                standardInput = yesInputStream
                println("exec: ${this.commandLine.joinToString(separator = " ")}")
            }.apply { println("ExecResult: $this") }
        }
    }
}

tasks.register("devAll") {
    group = CI_GRADLE
    doLast {
        gradlew("clean")
        gradlew("ktlintFormat")
        gradlew(
            "ciLint",
            "ciUnitTest",
            "ciRecordScreenshots",
            "ciBuildApp",
            "assembleAndroidTest",
        )
    }
}

tasks.register("devAllEmulator") {
    group = CI_GRADLE
    doLast {
        gradlew(
            "clean",
            "ciEmulatorJobsMatrixSetup",
        )
        gradlew("cleanManagedDevices", "--unused-only")
        EmulatorJobsMatrix().getTaskList(rootProject = rootProject).map { it.split(" ") }
            .forEach { tasks -> gradlew(*tasks.toTypedArray()) }
        gradlew("cleanManagedDevices")
    }
}

fun gradlew(
    vararg tasks: String,
    addToSystemProperties: Map<String, String>? = null,
) {
    providers.exec {
        executable = File(
            project.rootDir,
            if (Os.isFamily(Os.FAMILY_WINDOWS)) "gradlew.bat" else "gradlew",
        )
            .also { it.setExecutable(true) }
            .absolutePath
        args = mutableListOf<String>().also { mutableArgs ->
            mutableArgs.addAll(tasks)
            addToSystemProperties?.toList()?.map { "-D${it.first}=${it.second}" }?.let {
                mutableArgs.addAll(it)
            }
            mutableArgs.add("--stacktrace")
        }
        val sdkDirPath = Properties().apply {
            val propertiesFile = File(rootDir, "local.properties")
            if (propertiesFile.exists()) {
                load(propertiesFile.inputStream())
            }
        }.getProperty("sdk.dir")
        if (sdkDirPath != null) {
            val platformToolsDir = "$sdkDirPath${java.io.File.separator}platform-tools"
            val pathEnvironment = System.getenv("PATH").orEmpty()
            if (!pathEnvironment.contains(platformToolsDir)) {
                environment = environment.toMutableMap().apply {
                    put("PATH", "$platformToolsDir:$pathEnvironment")
                }
            }
        }
        if (System.getenv("JAVA_HOME") == null) {
            System.getProperty("java.home")?.let { javaHome ->
                environment = environment.toMutableMap().apply { put("JAVA_HOME", javaHome) }
            }
        }
        if (System.getenv("ANDROID_HOME") == null) {
            environment = environment.toMutableMap().apply { put("ANDROID_HOME", sdkDirPath) }
        }
        println("commandLine: ${this.commandLine.joinToString(separator = " ")}")
    }.apply { println("ExecResult: ${this.result.get()}") }
}
