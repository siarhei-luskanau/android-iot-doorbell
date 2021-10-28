import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream
import java.util.Properties
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
val COMMANDLINETOOLS_VERSION = "7583922"
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

        val commandlinetoolsUrl = if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            COMMANDLINETOOLS_WIN
        } else {
            COMMANDLINETOOLS_LINUX
        }

        exec {
            commandLine = listOf(
                "curl",
                commandlinetoolsUrl,
                "-o",
                "${config.androidHome}/commandlinetools-linux.zip",
            )
            standardInput = "yes\n".byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                "rm",
                "-rf",
                "${config.androidHome}/cmdline-tools/",
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                "unzip",
                "${config.androidHome}/commandlinetools-linux.zip",
                "-d",
                "${config.androidHome}/",
            )
            standardInput = "yes\n".byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                "rm",
                "${config.androidHome}/commandlinetools-linux.zip",
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                "${config.sdkmanager}",
                "--sdk_root=${config.androidHome}",
                "--licenses",
            )
            standardInput = "yes\n".byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--sdk_root=${config.androidHome}",
                "cmdline-tools;${BuildVersions.cmdlineToolsVersion}",
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
                "--sdk_root=${config.androidHome}",
                "--update",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--sdk_root=${config.androidHome}",
                "--licenses",
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = mutableListOf(
                config.sdkmanager.absolutePath,
                "--sdk_root=${config.androidHome}",
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
            }
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine =
                listOf(config.sdkmanager.absolutePath, "--sdk_root=${config.androidHome}", "--list")
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

tasks.register("moveScreenshotsFromDevices") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE

    doLast {
        val buildScreenshotsDirectory = File(project.buildDir, "screenshots")
            .apply { mkdirs() }

        config.getDevicesList().forEach { emulatorAttributes ->
            println("MoveScreenshotsFromDevices: $emulatorAttributes")
            exec {
                val emulatorName = emulatorAttributes.first().let { emulatorAttribute ->
                    ANDROID_EMULATORS
                        .filter { emulatorConfig -> emulatorAttribute.contains(emulatorConfig.port) }
                        .map { it.avdName }
                        .firstOrNull()
                        ?: emulatorAttribute
                }
                commandLine = listOf(
                    config.adb.absolutePath,
                    "-s",
                    emulatorAttributes.first(),
                    "pull",
                    "/sdcard/Pictures/screenshots/",
                    File(buildScreenshotsDirectory, emulatorName)
                        .apply { mkdirs() }
                        .absolutePath
                )
                isIgnoreExitValue = true
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }

            exec {
                commandLine = listOf(
                    config.adb.absolutePath,
                    "-s",
                    emulatorAttributes.first(),
                    "shell",
                    "rm",
                    "-rf",
                    "/sdcard/Pictures/screenshots/",
                )
                isIgnoreExitValue = true
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
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
