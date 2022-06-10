import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.util.Properties
import java.util.zip.ZipFile

// sudo apt-get install curl unzip openjdk-11-jdk
// export ANDROID_HOME=$HOME/Android/Sdk
// export PATH=$PATH:$ANDROID_HOME/cmdline-tools/bin
// ls ${ANDROID_HOME}/
// rm -r ${ANDROID_HOME}/
// mkdir -p ${ANDROID_HOME}/
// curl https://dl.google.com/android/repository/commandlinetools-linux-6858069_latest.zip -o ${ANDROID_HOME}/commandlinetools-linux.zip
// unzip ${ANDROID_HOME}/commandlinetools-linux.zip -d ${ANDROID_HOME}/
// rm ${ANDROID_HOME}/commandlinetools-linux.zip
// yes | ${ANDROID_HOME}/cmdline-tools/bin/sdkmanager --sdk_root=${ANDROID_HOME} --licenses
// ./gradlew :setupAndroidSDK :setupAndroidEmulator

val YES_INPUT: String = mutableListOf<String>()
    .apply { repeat(10) { add("y\n") } }
    .joinToString()

val EMULATOR_GRADLE = "EMULATOR_GRADLE"
val COMMANDLINETOOLS_VERSION = "8512546"
val COMMANDLINETOOLS_LINUX =
    "https://dl.google.com/android/repository/commandlinetools-linux-" +
            "${COMMANDLINETOOLS_VERSION}_latest.zip"
val COMMANDLINETOOLS_MAC =
    "https://dl.google.com/android/repository/commandlinetools-mac-" +
            "${COMMANDLINETOOLS_VERSION}_latest.zip"
val COMMANDLINETOOLS_WIN =
    "https://dl.google.com/android/repository/commandlinetools-win-" +
            "${COMMANDLINETOOLS_VERSION}_latest.zip"

tasks.register("setupAndroidCmdlineTools") {
    group = EMULATOR_GRADLE
    doLast {
        val config = AndroidSdkConfig()
            .apply { printSdkPath() }

        val commandlinetoolsUrl = when {
            Os.isFamily(Os.FAMILY_WINDOWS) -> COMMANDLINETOOLS_WIN
            Os.isFamily(Os.FAMILY_MAC) -> COMMANDLINETOOLS_MAC
            else -> COMMANDLINETOOLS_LINUX
        }

        val commandlinetoolsPath = "${config.androidHome}/commandlinetools.zip"
        println("downloading: $commandlinetoolsUrl")
        URL(commandlinetoolsUrl).openStream().use { input ->
            FileOutputStream(File(commandlinetoolsPath)).use { output ->
                input.copyTo(output)
            }
        }
        println("downloaded: $commandlinetoolsUrl")

        "${config.androidHome}/cmdline-tools/".also {
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
                "${config.sdkmanager}",
                "--licenses",
                "--sdk_root=${config.androidHome}",
                "--channel=3",
            )
            standardInput = "yes\n".byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "cmdline-tools;${BuildVersions.cmdlineToolsVersion}",
                "--sdk_root=${config.androidHome}",
                "--channel=3",
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
        val config = AndroidSdkConfig()
            .apply { printSdkPath() }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--update",
                "--sdk_root=${config.androidHome}",
                "--channel=3",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--licenses",
                "--sdk_root=${config.androidHome}",
                "--channel=3",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = mutableListOf(
                config.sdkmanager.absolutePath,
                "platform-tools",
                "build-tools;${BuildVersions.buildToolsVersion}",
                "platforms;android-${BuildVersions.platformVersion}",
            ).apply {
                val avdName = System.getenv(ENV_EMULATOR_AVD_NAME).orEmpty()
                val emulatorConfig = ANDROID_EMULATORS.find { it.avdName == avdName }
                if (emulatorConfig != null) {
                    add("emulator")
                    add(emulatorConfig.sdkId)
                }
            }.apply {
                addAll(
                    listOf(
                        "--sdk_root=${config.androidHome}",
                        "--channel=3",
                    )
                )
            }
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--list",
                "--sdk_root=${config.androidHome}",
                "--channel=3",
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register("setupAndroidEmulator") {
    group = EMULATOR_GRADLE

    doLast {
        val config = AndroidSdkConfig()
            .apply { printSdkPath() }

        val avdName = System.getenv(ENV_EMULATOR_AVD_NAME).orEmpty()

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
                        config.avdmanager.absolutePath,
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
            commandLine = listOf(config.avdmanager.absolutePath, "-v", "list", "avd")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register("runAndroidEmulator") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE

    doLast {
        val avdName = System.getenv(ENV_EMULATOR_AVD_NAME).orEmpty()
            .also { println("System.getenv(${ENV_EMULATOR_AVD_NAME}): $it") }
        val emulatorConfig = requireNotNull(ANDROID_EMULATORS.find { it.avdName == avdName })
            .also { println("EmulatorConfig: $it") }

        // https://developer.android.com/studio/run/emulator-commandline#startup-options
        val commandArgs = mutableListOf(
            config.emulator.absolutePath,
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
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE

    doLast {
        var i = 0
        while (true) {
            Thread.sleep(1_000)
            i++
            if (i > 1000) {
                break
            }
            var result: String? = null
            config.getDevicesList()
                .also {
                    if (it.isEmpty()) {
                        println("WaitAndroidEmulator: Wait $i: No emulators found!")
                    }
                }
                .forEach { emulatorAttributes ->
                    println("WaitAndroidEmulator:  Wait $i: $emulatorAttributes")
                    if (emulatorAttributes.contains("offline")) {
                        println("WaitAndroidEmulator: offline")
                    } else {
                        val currentTimeMillis = System.currentTimeMillis()
                        val resultOutputStream = ByteArrayOutputStream()
                        exec {
                            commandLine = listOf(
                                config.adb.absolutePath,
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
                break
            }
        }
    }
}

tasks.register("killAndroidEmulator") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE

    doLast {
        config.getDevicesList().forEach { emulatorAttributes ->
            println("KillAndroidEmulator: $emulatorAttributes")
            exec {
                commandLine = listOf(
                    config.adb.absolutePath,
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

private class AndroidSdkConfig {
    val androidHome = readAndroidSdkLocation()
    val sdkmanager = sdkFile(
        "cmdline-tools",
        "bin",
        platformExecutable(name = "sdkmanager", ext = "bat")
    )
    val avdmanager = sdkFile(
        "cmdline-tools",
        BuildVersions.cmdlineToolsVersion,
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
        File(readAndroidSdkLocation(), path.joinToString(File.separator))

    private fun readAndroidSdkLocation(): String =
        readAndroidSdkFromLocalProperties()
            ?: run { System.getenv("ANDROID_HOME") }
            ?: throw Error("Android sdk isn't defined in local properties or environment variable")

    private fun readAndroidSdkFromLocalProperties(): String? {
        val localProperties = File(project.rootDir, "local.properties")
        return if (localProperties.exists()) {
            Properties().apply {
                load(localProperties.inputStream())
            }.getProperty("sdk.dir")
        } else {
            null
        }
    }
}
