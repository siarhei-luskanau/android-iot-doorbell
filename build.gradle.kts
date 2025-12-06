@file:Suppress("PropertyName")

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.Properties
import org.apache.tools.ant.taskdefs.condition.Os

println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
System.getProperties().forEach { (key, value) -> println("System.getProperties(): $key=$value") }
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
        ":di:di_koin"
    ).contains(it.path)
}.forEach {
    it.apply(plugin = "org.jetbrains.kotlinx.kover")
    it.dependencies { kover(project(it.path)) }
}

val CI_GRADLE = "CI_GRADLE"

tasks.register("ciLint") {
    group = CI_GRADLE
    doLast {
        val injected = project.objects.newInstance<Injected>()
        injected.gradlew(
            "ktlintCheck",
            "detekt",
            "lintDebug"
        )
    }
}

tasks.register("ciUnitTest") {
    group = CI_GRADLE
    doLast {
        val injected = project.objects.newInstance<Injected>()
        injected.gradlew(
            "clean"
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
        val injected = project.objects.newInstance<Injected>()
        injected.gradlew("updateDebugScreenshotTest")
    }
}

tasks.register("ciBuildApp") {
    group = CI_GRADLE
    doLast {
        val injected = project.objects.newInstance<Injected>()
        injected.gradlew("assembleDebug", "assembleRelease")
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
    val injected = project.objects.newInstance<Injected>()
    doLast {
        val sdkDirPath = injected.getAndroidSdkPath()
        injected.getSdkManagerFile()?.let { sdkManagerFile ->
            val yesInputStream = object : InputStream() {
                private val yesString = "y\n"
                private var counter = 0
                override fun read(): Int = yesString[counter % 2].also { counter++ }.code
            }
            injected.execOperations.exec {
                commandLine =
                    listOf(sdkManagerFile.absolutePath, "--list", "--sdk_root=$sdkDirPath")
                println("exec: ${this.commandLine.joinToString(separator = " ")}")
            }.apply { println("ExecResult: ${this.exitValue}") }
            injected.execOperations.exec {
                commandLine =
                    listOf(sdkManagerFile.absolutePath, "--licenses", "--sdk_root=$sdkDirPath")
                standardInput = yesInputStream
                println("exec: ${this.commandLine.joinToString(separator = " ")}")
            }.apply { println("ExecResult: ${this.exitValue}") }
        }
    }
}

tasks.register("devAll") {
    group = CI_GRADLE
    doLast {
        val injected = project.objects.newInstance<Injected>()
        injected.gradlew("clean")
        injected.gradlew("ktlintFormat")
        injected.gradlew(
            "ciLint",
            "ciUnitTest",
            "ciRecordScreenshots",
            "ciBuildApp",
            "assembleAndroidTest"
        )
    }
}

tasks.register("devAllEmulator") {
    group = CI_GRADLE
    doLast {
        val injected = project.objects.newInstance<Injected>()
        injected.gradlew(
            "clean",
            "ciEmulatorJobsMatrixSetup"
        )
        injected.gradlew("cleanManagedDevices", "--unused-only")
        EmulatorJobsMatrix()
            .getTaskList(rootProject = rootProject)
            .map { it.split(" ") }
            .forEach { tasks -> injected.gradlew(*tasks.toTypedArray()) }
        injected.gradlew("cleanManagedDevices")
    }
}

abstract class Injected {

    @get:Inject abstract val fs: FileSystemOperations

    @get:Inject abstract val execOperations: ExecOperations

    @get:Inject abstract val projectLayout: ProjectLayout

    fun gradlew(vararg tasks: String, addToSystemProperties: Map<String, String>? = null) {
        execOperations.exec {
            commandLine = mutableListOf<String>().also { mutableArgs ->
                mutableArgs.add(
                    projectLayout.projectDirectory.file(
                        if (Os.isFamily(Os.FAMILY_WINDOWS)) "gradlew.bat" else "gradlew"
                    ).asFile.path
                )
                mutableArgs.addAll(tasks)
                addToSystemProperties?.toList()?.map { "-D${it.first}=${it.second}" }?.let {
                    mutableArgs.addAll(it)
                }
                mutableArgs.add("--stacktrace")
            }
            val sdkDirPath = Properties().apply {
                val propertiesFile = projectLayout.projectDirectory.file("local.properties").asFile
                if (propertiesFile.exists()) {
                    load(propertiesFile.inputStream())
                }
            }.getProperty("sdk.dir")
            if (sdkDirPath != null) {
                val platformToolsDir = "$sdkDirPath${File.separator}platform-tools"
                val pathEnvironment = System.getenv("PATH").orEmpty()
                if (!pathEnvironment.contains(platformToolsDir)) {
                    environment = environment.toMutableMap().apply {
                        put("PATH", "$platformToolsDir:$pathEnvironment")
                    }
                }
            }
            if (System.getenv("JAVA_HOME") == null) {
                System.getProperty("java.home")?.let { javaHome ->
                    environment = environment.toMutableMap().apply {
                        put("JAVA_HOME", javaHome)
                    }
                }
            }
            if (System.getenv("ANDROID_HOME") == null) {
                environment = environment.toMutableMap().apply {
                    put("ANDROID_HOME", sdkDirPath)
                }
            }
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: ${this.exitValue}") }
    }

    fun runExec(commands: List<String>): String = object : ByteArrayOutputStream() {
        override fun write(p0: ByteArray, p1: Int, p2: Int) {
            print(String(p0, p1, p2))
            super.write(p0, p1, p2)
        }
    }.let { resultOutputStream ->
        execOperations.exec {
            if (System.getenv("JAVA_HOME") == null) {
                System.getProperty("java.home")?.let { javaHome ->
                    environment = environment.toMutableMap().apply {
                        put("JAVA_HOME", javaHome)
                    }
                }
            }
            commandLine = commands
            standardOutput = resultOutputStream
            println("commandLine: ${this.commandLine.joinToString(separator = " ")}")
        }.apply { println("ExecResult: $this") }
        String(resultOutputStream.toByteArray())
    }

    fun getAndroidSdkPath(): String? = Properties().apply {
        val propertiesFile = File(projectLayout.projectDirectory.asFile, "local.properties")
        if (propertiesFile.exists()) {
            load(propertiesFile.inputStream())
        }
    }.getProperty("sdk.dir").let { propertiesSdkDirPath ->
        (propertiesSdkDirPath ?: System.getenv("ANDROID_HOME"))
    }

    fun getSdkManagerFile(): File? = getAndroidSdkPath()?.let { sdkDirPath ->
        println("sdkDirPath: $sdkDirPath")
        val files = File(sdkDirPath).walk().filter { file ->
            file.path.contains("cmdline-tools") && file.path.endsWith("sdkmanager")
        }
        files.forEach { println("walk: ${it.absolutePath}") }
        val sdkmanagerFile = files.firstOrNull()
        println("sdkmanagerFile: $sdkmanagerFile")
        sdkmanagerFile
    }
}
