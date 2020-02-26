import org.apache.tools.ant.taskdefs.condition.Os
import java.io.ByteArrayOutputStream
import java.util.Properties

// sudo apt-get install curl unzip openjdk-8-jdk
// export ANDROID_HOME=$HOME/Android/Sdk
// export PATH=$PATH:$ANDROID_HOME/tools
// ls ${ANDROID_HOME}/
// rm -r ${ANDROID_HOME}/
// mkdir -p ${ANDROID_HOME}/
// curl https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip -o ${ANDROID_HOME}/android-sdk.zip
// unzip ${ANDROID_HOME}/android-sdk.zip -d ${ANDROID_HOME}/
// rm ${ANDROID_HOME}/android-sdk.zip
// ./gradlew :setupAndroidSDK :setupAndroidEmulator

data class EmulatorConfig(
    val avdName: String,
    val sdkId: String,
    val deviceType: String,
    val port: String
)

val ANDROID_EMULATORS = listOf(
//    EmulatorConfig(
//        avdName = "TestEmulator29",
//        sdkId = "system-images;android-29;google_apis;x86",
//        deviceType = "Nexus 5X",
//        port = "5564"
//    )
//    ,
    EmulatorConfig(
        avdName = "TestEmulator28",
        sdkId = "system-images;android-28;google_apis;x86_64",
        deviceType = "Galaxy Nexus",
        port = "5562"
    )
//    ,
//    EmulatorConfig(
//        avdName = "TestEmulator23",
//        sdkId = "system-images;android-23;google_apis;x86_64",
//        deviceType = "Nexus One",
//        port = "5560"
//    )
)

val YES_INPUT: String = mutableListOf<String>()
    .apply { repeat(10) { add("y\n") } }
    .joinToString()

val EMULATOR_GRADLE = "EMULATOR_GRADLE"

tasks.register<Exec>("setupAndroidSDK") {
    group = EMULATOR_GRADLE
    commandLine = listOf("java", "-version")

    doLast {
        val config = AndroidSdkConfig()
            .apply { this.printSdkPath() }

        exec {
            commandLine = listOf(config.sdkmanager.absolutePath, "tools")
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(config.sdkmanager.absolutePath, "--update")
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(config.sdkmanager.absolutePath, "--licenses")
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = mutableListOf(
                config.sdkmanager.absolutePath,
                "tools",
                "platform-tools",
                "build-tools;30.0.0-rc2",
                "platforms;android-29",
                "emulator"
            ).apply { addAll(ANDROID_EMULATORS.map { it.sdkId }) }
            standardInput = YES_INPUT.byteInputStream()
            standardOutput = ByteArrayOutputStream()
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }

        exec {
            commandLine = listOf(config.sdkmanager.absolutePath, "--list")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register<Exec>("setupAndroidEmulator") {
    group = EMULATOR_GRADLE
    commandLine = listOf("java", "-version")

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
                    "500M",
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

tasks.register<Exec>("runAndroidEmulator") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE
    commandLine = listOf(config.adb.absolutePath, "start-server")

    doLast {
        Thread.sleep(3000)
        ANDROID_EMULATORS.forEach { emulatorConfig ->
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
                        // https://developer.android.com/studio/run/emulator-acceleration.html#command-gpu
                        "-gpu",
                        "swiftshader_indirect",
                        // "-no-window",
                        "-no-audio",
                        "-no-snapshot"
                    )
                    .apply { println("ProcessBuilder: ${this.command()}") }
                    .start()

                Thread.sleep(3000)

                if (process.isAlive.not() && process.exitValue() != 0) {
                    println(process.errorStream.bufferedReader().use { it.readText() })
                    println(process.inputStream.bufferedReader().use { it.readText() })
                    throw Error("Failed to start process")
                }
            }.start()
        }

        Thread.sleep(30 * 1000)
        exec {
            commandLine = listOf(config.adb.absolutePath, "devices", "-l")
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register<Exec>("waitAndroidEmulator") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE
    commandLine = listOf("java", "-version")

    doLast {
        config.getDevicesList().forEach { emulatorAttributes ->
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

tasks.register<Exec>("moveScreenshotsFromDevices") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE
    commandLine = listOf(config.adb.absolutePath, "start-server")

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
                    "/sdcard/Pictures/screenshots/.",
                    File(buildScreenshotsDirectory, emulatorName)
                        .apply { mkdirs() }
                        .absolutePath
                )
                isIgnoreExitValue = true
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }
    }
}

tasks.register<Exec>("killAndroidEmulator") {
    val config = AndroidSdkConfig()
    group = EMULATOR_GRADLE
    commandLine = listOf(config.adb.absolutePath, "devices", "-l")

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
    val sdkmanager = sdkFile("tools", "bin", platformExecutable(name = "sdkmanager", ext = "bat"))
    val avdmanager = sdkFile("tools", "bin", platformExecutable(name = "avdmanager", ext = "bat"))
    val emulator = sdkFile("emulator", platformExecutable(name = "emulator"))
    val adb = sdkFile("platform-tools", platformExecutable(name = "adb"))

    fun getDevicesList(): List<List<String>> =
        ByteArrayOutputStream().also { devicesOutput ->
            exec {
                commandLine = listOf(adb.absolutePath, "devices", "-l")
                standardOutput = devicesOutput
                println("commandLine: ${this.commandLine}")
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

    private fun platformExecutable(name: String, ext: String = "exe"): String =
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            "$name.$ext"
        } else {
            name
        }

    private fun readAndroidSdkLocation(): String =
        readAndroidSdkFromLocalProperties()?.let { it }
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
