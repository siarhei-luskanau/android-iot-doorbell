import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.Properties
import java.util.zip.ZipFile

println("gradle.startParameter.taskNames: ${gradle.startParameter.taskNames}")
System.getProperties().forEach { key, value -> println("System.Property: $key=$value") }
System.getenv().forEach { (key, value) -> println("System.env: $key=$value") }

val YES_INPUT: String = mutableListOf<String>()
    .apply { repeat(10) { add("y\n") } }
    .joinToString()

val EMULATOR_GRADLE = "EMULATOR_GRADLE"
val COMMANDLINETOOLS_VERSION = "9123335"
val COMMANDLINETOOLS_LINUX =
    "https://dl.google.com/android/repository/commandlinetools-linux-" +
            "${COMMANDLINETOOLS_VERSION}_latest.zip"
val COMMANDLINETOOLS_MAC =
    "https://dl.google.com/android/repository/commandlinetools-mac-" +
            "${COMMANDLINETOOLS_VERSION}_latest.zip"
val COMMANDLINETOOLS_WIN =
    "https://dl.google.com/android/repository/commandlinetools-win-" +
            "${COMMANDLINETOOLS_VERSION}_latest.zip"
val CMDLINE_TOOLS_VERSION = "8.0"

val androidSdkConfig = AndroidSdkConfig()

tasks.register("setupAndroidCmdlineTools") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        val commandlinetoolsUrl = when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> COMMANDLINETOOLS_WIN
            Os.isFamily(Os.FAMILY_MAC) -> COMMANDLINETOOLS_MAC
            else -> COMMANDLINETOOLS_LINUX
        }

        val commandlinetoolsPath = "${androidSdkConfig.sdkDirPath}/commandlinetools.zip"

        File(commandlinetoolsPath).parentFile.let {
            println("creating folder: $it")
            it.mkdirs()
        }

        println("downloading: $commandlinetoolsUrl")
        URL(commandlinetoolsUrl).openStream().use { input ->
            FileOutputStream(File(commandlinetoolsPath)).use { output ->
                input.copyTo(output)
            }
        }
        println("downloaded: $commandlinetoolsUrl")

        "${androidSdkConfig.sdkDirPath}/cmdline-tools/".also {
            println("deleting: $it")
            File(it).deleteRecursively()
            println("deleted: $it")
        }

        println("unzupping: $commandlinetoolsPath")
        val destDirectory = File(commandlinetoolsPath).parentFile
        ZipFile(commandlinetoolsPath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val outputFile = File(destDirectory, entry.name)
                    outputFile.parentFile.mkdirs()
                    outputFile.outputStream().use { input.copyTo(it) }
                    outputFile.setExecutable(true)
                }
            }
        }
        println("unzupped: $commandlinetoolsPath")

        println("deleting: $commandlinetoolsPath")
        File(commandlinetoolsPath).deleteRecursively()
        println("deleted: $commandlinetoolsPath")

        exec {
            commandLine = listOf(
                "${androidSdkConfig.sdkmanager}",
                "--licenses",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "cmdline-tools;$CMDLINE_TOOLS_VERSION",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register("setupAndroidSDK") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        exec {
            commandLine = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "--update",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "--licenses",
                "--sdk_root=${androidSdkConfig.sdkDirPath}"
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = mutableListOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "platform-tools",
            ).apply {
                val avdName = System.getProperty(GradleArguments.EMULATOR_AVD_NAME).orEmpty()
                val emulatorConfig = ANDROID_EMULATORS.find { it.avdName == avdName }
                if (emulatorConfig != null) {
                    add("emulator")
                    add(emulatorConfig.sdkId)
                }
            }.apply {
                add("--sdk_root=${androidSdkConfig.sdkDirPath}")
            }
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "--list",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register("setupAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        val avdName = requireNotNull(System.getProperty(GradleArguments.EMULATOR_AVD_NAME)) {
            "Please provide EMULATOR_AVD_NAME argument"
        }

        ANDROID_EMULATORS
            .filter {
                if (avdName.isNotEmpty()) {
                    it.avdName == avdName
                } else {
                    true
                }
            }
            .forEach { emulatorConfig ->
                exec {
                    commandLine = listOf(
                        androidSdkConfig.avdmanager.absolutePath,
                        "-v",
                        "create",
                        "avd",
                        "--force",
                        "-n",
                        emulatorConfig.avdName,
                        "--device",
                        emulatorConfig.deviceType,
                        "-k",
                        emulatorConfig.sdkId,
                    )
                    standardOutput = ByteArrayOutputStream()
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }
            }

        exec {
            commandLine = listOf(androidSdkConfig.avdmanager.absolutePath, "-v", "list", "avd")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register("runAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        exec {
            commandLine = listOf(androidSdkConfig.adb.absolutePath, "start-server")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        val avdName = System.getProperty(GradleArguments.EMULATOR_AVD_NAME).orEmpty()
            .also { println("System.getProperty(${GradleArguments.EMULATOR_AVD_NAME}): $it") }
        val emulatorConfig = requireNotNull(ANDROID_EMULATORS.find { it.avdName == avdName })
            .also { println("EmulatorConfig: $it") }

        // https://developer.android.com/studio/run/emulator-commandline#startup-options
        val commandArgs = mutableListOf(
            androidSdkConfig.emulator.absolutePath,
            "-avd",
            emulatorConfig.avdName,
            "-port",
            emulatorConfig.port,
        )
        emulatorConfig.memory?.let { memory ->
            commandArgs.addAll(listOf("-memory", memory))
        }
        emulatorConfig.partitionSize?.let { partitionSize ->
            commandArgs.addAll(listOf("-partition-size", partitionSize))
        }
        commandArgs.addAll(
            listOf(
                "-accel",
                "auto",
                "-gpu",
                "auto",
                "-no-snapshot",
                "-no-audio",
                "-no-boot-anim",
                // "-no-window",
            )
        )

        exec {
            commandLine = commandArgs
            println("Start emulator: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register("waitAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        var isEmulatorFound = false
        for (i in 1..300) {
            Thread.sleep(1_000)
            var result: String? = null
            androidSdkConfig.getDevicesList()
                .also {
                    if (it.isEmpty()) {
                        println("WaitAndroidEmulator: Wait $i: No emulators found!")
                    }
                }
                .forEach { emulatorAttributes ->
                    println("WaitAndroidEmulator: Wait $i: $emulatorAttributes")
                    if (emulatorAttributes.contains("offline")) {
                        println("WaitAndroidEmulator: offline")
                    } else {
                        val resultOutputStream = ByteArrayOutputStream()
                        exec {
                            commandLine = listOf(
                                androidSdkConfig.adb.absolutePath,
                                "-s",
                                emulatorAttributes.first(),
                                "wait-for-device",
                                "shell",
                                "getprop sys.boot_completed",
                            )
                            standardOutput = resultOutputStream
                            isIgnoreExitValue = true
                            println("commandLine: ${this.commandLine}")
                        }.apply { println("ExecResult: $this") }
                        result = String(resultOutputStream.toByteArray()).trim()
                    }
                }
            println("sys.boot_completed = $result")
            if (result == "1") {
                println("Emulator booted")
                isEmulatorFound = true
                break
            }
        }
        if (isEmulatorFound.not()) {
            throw IllegalStateException("Emulator not found")
        }
    }
}

tasks.register("killAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        androidSdkConfig.getDevicesList().forEach { emulatorAttributes ->
            println("KillAndroidEmulator: $emulatorAttributes")
            exec {
                commandLine = listOf(
                    androidSdkConfig.adb.absolutePath,
                    "-s",
                    emulatorAttributes.first(),
                    "emu",
                    "kill",
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }
    }
}

tasks.register("deleteAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        ANDROID_EMULATORS.forEach { emulatorConfig ->
            runCatching {
                exec {
                    commandLine = listOf(
                        androidSdkConfig.avdmanager.absolutePath,
                        "-v",
                        "delete",
                        "avd",
                        "-n",
                        emulatorConfig.avdName,
                    )
                    standardOutput = ByteArrayOutputStream()
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }
            }
        }

        exec {
            commandLine = listOf(androidSdkConfig.avdmanager.absolutePath, "-v", "list", "avd")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

class AndroidSdkConfig {

    val sdkDirPath: String by lazy {
        readAndroidSdkFromLocalProperties()
            ?: System.getenv("ANDROID_HOME")
            ?: System.getenv("sdk.dir")
            ?: pathOf(
                System.getProperty("user.home"),
                if (Os.isFamily(Os.FAMILY_MAC)) "Library" else null,
                "Android",
                if (Os.isFamily(Os.FAMILY_MAC)) "sdk" else "Sdk"
            )
    }

    init {
        writeAndroidSdkToLocalProperties()
    }

    val sdkmanager = sdkFile(
        "cmdline-tools",
        "bin",
        platformExecutable(name = "sdkmanager", ext = "bat")
    )
    val avdmanager = sdkFile(
        "cmdline-tools",
        CMDLINE_TOOLS_VERSION,
        "bin",
        platformExecutable(name = "avdmanager", ext = "bat")
    )
    val emulator = sdkFile("emulator", platformExecutable(name = "emulator"))
    val adb = sdkFile("platform-tools", platformExecutable(name = "adb"))

    fun getDevicesList(): List<List<String>> =
        ByteArrayOutputStream().also { devicesOutput ->
            exec {
                commandLine = listOf(adb.absolutePath, "devices", "-l")
                standardOutput = devicesOutput
                isIgnoreExitValue = true
                println("getDevicesList: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }.let { devicesOutput ->
            println(devicesOutput)
            String(devicesOutput.toByteArray())
                .split("\n")
                .filter { it.contains("emulator-") }
                .map { deviceString ->
                    deviceString.split(" ")
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                }
        }

    fun printSdkPath() {
        println("sdk: ${sdkFile().exists()}: ${sdkFile()}")
        println("sdkmanager: ${sdkmanager.exists()}: $sdkmanager")
        println("avdmanager: ${avdmanager.exists()}: $avdmanager")
        println("emulator: ${emulator.exists()}: $emulator")
        println("adb: ${adb.exists()}: $adb")
    }

    private fun sdkFile(vararg path: String) =
        File(sdkDirPath, path.joinToString(File.separator))

    private fun readAndroidSdkFromLocalProperties(): String? =
        Properties().apply {
            val propertiesFile = File(rootDir.parentFile, "local.properties")
            if (propertiesFile.exists()) {
                load(propertiesFile.inputStream())
            }
        }.getProperty(SDK_DIR)

    private fun writeAndroidSdkToLocalProperties() {
        val propertiesFile = File(rootDir.parentFile, "local.properties")
        val properties = Properties().apply {
            if (propertiesFile.exists()) {
                load(propertiesFile.inputStream())
            }
        }
        properties[SDK_DIR] = sdkDirPath
        propertiesFile.outputStream().use { output ->
            properties.store(output, "local.properties")
        }
    }

    companion object {
        private const val SDK_DIR = "sdk.dir"
    }
}
