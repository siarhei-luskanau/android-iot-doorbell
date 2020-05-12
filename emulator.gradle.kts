import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream
import java.util.Properties

// sudo apt-get install curl unzip openjdk-8-jdk
// export ANDROID_HOME=$HOME/Android/Sdk
// export PATH=$PATH:$ANDROID_HOME/tools/bin
// ls ${ANDROID_HOME}/
// rm -r ${ANDROID_HOME}/
// mkdir -p ${ANDROID_HOME}/
// curl https://dl.google.com/android/repository/commandlinetools-linux-6200805_latest.zip -o ${ANDROID_HOME}/commandlinetools-linux.zip
// unzip ${ANDROID_HOME}/commandlinetools-linux.zip -d ${ANDROID_HOME}/
// rm ${ANDROID_HOME}/commandlinetools-linux.zip
// yes | ${ANDROID_HOME}/tools/bin/sdkmanager --sdk_root=${ANDROID_HOME} --licenses 
// ./gradlew :setupAndroidSDK :setupAndroidEmulator

val YES_INPUT: String = mutableListOf<String>()
    .apply { repeat(10) { add("y\n") } }
    .joinToString()

val EMULATOR_GRADLE = "EMULATOR_GRADLE"

tasks.register("setupAndroidSDK") {
    group = EMULATOR_GRADLE

    doLast {
        val config = AndroidSdkConfig()
            .apply { this.printSdkPath() }

        exec {
            commandLine =
                listOf(config.sdkmanager.absolutePath, "--sdk_root=${config.androidHome}", "tools")
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--sdk_root=${config.androidHome}",
                "--update"
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(
                config.sdkmanager.absolutePath,
                "--sdk_root=${config.androidHome}",
                "--licenses"
            )
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = mutableListOf(
                config.sdkmanager.absolutePath,
                "--sdk_root=${config.androidHome}",
                "tools",
                "platform-tools",
                "build-tools;${BuildVersions.buildToolsVersion}",
                "platforms;android-${BuildVersions.platformVersion}",
                "emulator"
            ).apply { addAll(ANDROID_EMULATORS.map { it.sdkId }) }
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
            .apply { this.printSdkPath() }

        ANDROID_EMULATORS.forEach { emulatorConfig ->
            exec {
                commandLine = listOf(
                    config.avdmanager.absolutePath,
                    "-v",
                    "delete",
                    "avd",
                    "-n",
                    emulatorConfig.avdName
                )
                isIgnoreExitValue = true
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }

        ANDROID_EMULATORS.forEach { emulatorConfig ->
            exec {
                commandLine = listOf(
                    config.avdmanager.absolutePath,
                    "-v",
                    "create",
                    "avd",
                    "-n",
                    emulatorConfig.avdName,
                    "--sdcard",
                    "100M",
                    "--device",
                    emulatorConfig.deviceType,
                    "-k",
                    emulatorConfig.sdkId
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
        exec {
            commandLine = listOf(config.adb.absolutePath, "start-server")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        val avdName = System.getenv(ENV_EMULATOR_AVD_NAME).orEmpty()
            .also { println("System.getenv(${ENV_EMULATOR_AVD_NAME}): $it") }
        val emulatorConfig = requireNotNull(ANDROID_EMULATORS.find { it.avdName == avdName })
            .also { println("EmulatorConfig: $it") }

        Thread {
            println("Start emulator: ${emulatorConfig.avdName}")
            val process = ProcessBuilder()
                .directory(projectDir)
                .command(
                    config.emulator.absolutePath,
                    "-avd",
                    emulatorConfig.avdName,
                    "-port",
                    emulatorConfig.port,
                    // "-no-window",
                    "-no-boot-anim",
                    "-no-audio",
                    "-no-snapshot"
                )
                .apply { println("ProcessBuilder: ${this.command()}") }
                .start()

            for (i in 1..30) {
                if (process.isAlive.not()) {
                    Thread.sleep(1_000)
                } else {
                    break
                }
            }
            if (process.isAlive.not() && process.exitValue() != 0) {
                println(process.errorStream.bufferedReader().use { it.readText() })
                println(process.inputStream.bufferedReader().use { it.readText() })
                throw Error("Failed to start process")
            } else {
                println("Emulator is started.")
            }
        }.start()
    }
}

tasks.register("waitAndroidEmulator") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE

    doLast {
        for (i in 1..(6 * 5)) {
            if (config.getDevicesList().isEmpty()) {
                println("WaitAndroidEmulator: No emulators found! Wait $i...")
                Thread.sleep(10_000)
            } else {
                break
            }
        }

        config.getDevicesList()
            .also {
                if (it.isEmpty()) {
                    throw IllegalStateException("No emulators found!")
                }
            }
            .forEach { emulatorAttributes ->
                println("WaitAndroidEmulator: $emulatorAttributes")
                exec {
                    commandLine = listOf(
                        config.adb.absolutePath,
                        "-s",
                        emulatorAttributes.first(),
                        "wait-for-device",
                        "shell",
                        "while $(exit $(getprop sys.boot_completed)) ; do sleep 1; done;"
                    )
                    isIgnoreExitValue = true
                    println("commandLine: ${this.commandLine}")
                }.apply { println("ExecResult: $this") }
            }
    }
}

tasks.register("moveScreenshotsFromDevices") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE

    doLast {
        val buildScreenshotsDirectory = File(File(project.buildDir, "artifacts"), "screenshots")
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
                    "/sdcard/Pictures/screenshots/"
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
                    "kill"
                )
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }
    }
}

private class AndroidSdkConfig {
    val androidHome = readAndroidSdkLocation()
    val sdkmanager = sdkFile("tools", "bin", platformExecutable(name = "sdkmanager", ext = "bat"))
    val avdmanager = sdkFile("tools", "bin", platformExecutable(name = "avdmanager", ext = "bat"))
    val emulator = sdkFile("emulator", platformExecutable(name = "emulator"))
    val adb = sdkFile("platform-tools", platformExecutable(name = "adb"))

    fun getDevicesList(): List<List<String>> =
        ByteArrayOutputStream().also { devicesOutput ->
            exec {
                commandLine = listOf(adb.absolutePath, "devices", "-l")
                standardOutput = devicesOutput
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
