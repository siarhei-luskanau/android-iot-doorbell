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

val ANDROID_EMULATORS = mapOf(
    "TestEmulator29" to "system-images;android-29;google_apis;x86"
)
val YES_INPUT: String = mutableListOf<String>()
    .apply { repeat(10) { add("y\n") } }
    .joinToString()

tasks.register<Exec>("setupAndroidSDK") {
    group = "setupAndroid"
    description = "setupAndroidSDK"
    commandLine = listOf("java", "-version")

    doLast {
        val config = AndroidSdkConfig()

        println("sdkmanager: ${config.sdkmanager.exists()}: ${config.sdkmanager}")
        println("avdmanager: ${config.avdmanager.exists()}: ${config.avdmanager}")
        println("emulator: ${config.emulator.exists()}: ${config.emulator}")
        println("adb: ${config.adb.exists()}: ${config.adb}")

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
                "build-tools;29.0.2",
                "platforms;android-29",
                "emulator"
            ).apply { addAll(ANDROID_EMULATORS.values) }
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
    group = "setupAndroid"
    description = "setupAndroidEmulator"
    commandLine = listOf("java", "-version")

    doLast {
        val config = AndroidSdkConfig()

        println("sdkmanager: ${config.sdkmanager.exists()}: ${config.sdkmanager}")
        println("avdmanager: ${config.avdmanager.exists()}: ${config.avdmanager}")
        println("emulator: ${config.emulator.exists()}: ${config.emulator}")
        println("adb: ${config.adb.exists()}: ${config.adb}")

        ANDROID_EMULATORS.keys.forEach { emulatorName ->
            exec {
                commandLine = listOf(
                    config.avdmanager.absolutePath,
                    "-v",
                    "delete",
                    "avd",
                    "-n",
                    emulatorName
                )
                isIgnoreExitValue = true
                println("commandLine: ${this.commandLine}")
            }.apply { println("ExecResult: $this") }
        }

        ANDROID_EMULATORS.entries.forEach { emulatorEntry ->
            exec {
                commandLine = listOf(
                    config.avdmanager.absolutePath,
                    "-v",
                    "create",
                    "avd",
                    "-n",
                    emulatorEntry.key,
                    "--sdcard",
                    "500M",
                    "--device",
                    "Nexus 5X",
                    "-k",
                    emulatorEntry.value
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
    group = "runEmulator"
    description = "runAndroidEmulator"
    commandLine = listOf("java", "-version")

    doLast {
        val config = AndroidSdkConfig()
        ANDROID_EMULATORS.keys.forEach { emulatorName ->
            val process = ProcessBuilder()
                .directory(projectDir)
                .command(
                    config.emulator.absolutePath,
                    "-avd",
                    emulatorName,
                    // https://developer.android.com/studio/run/emulator-acceleration.html#command-gpu
                    "-gpu",
                    "swiftshader_indirect",
                    // "-no-window",
                    "-no-audio"
                )
                .apply { println("ProcessBuilder: ${this.command()}") }
                .start()

            Thread.sleep(1000)

            if (process.isAlive.not() && process.exitValue() != 0) {
                println(process.errorStream.bufferedReader().use { it.readText() })
                println(process.inputStream.bufferedReader().use { it.readText() })
                throw Error("Failed to start process")
            }
        }
    }
}

tasks.register<Exec>("waitAndroidEmulator") {
    group = "runEmulator"
    description = "waitAndroidEmulator"
    commandLine = listOf("java", "-version")

    doLast {
        val config = AndroidSdkConfig()

        exec {
            commandLine = listOf(
                config.adb.absolutePath,
                "wait-for-device",
                "shell",
                "while $(exit $(getprop sys.boot_completed)) ; do sleep 1; done;"
            )
            isIgnoreExitValue = true
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

tasks.register<Exec>("killAndroidEmulator") {
    group = "runEmulator"
    description = "killEmulator"
    commandLine = listOf("java", "-version")

    doLast {
        val config = AndroidSdkConfig()

        exec {
            commandLine = listOf(
                config.adb.absolutePath,
                "emu",
                "kill"
            )
            println("commandLine: ${this.commandLine}")
        }.apply { println("ExecResult: $this") }
    }
}

private class AndroidSdkConfig {
    val sdkmanager = sdkFile("tools", "bin", platformExecutable(name = "sdkmanager", ext = "bat"))
    val avdmanager = sdkFile("tools", "bin", platformExecutable(name = "avdmanager", ext = "bat"))
    val emulator = sdkFile("emulator", platformExecutable(name = "emulator"))
    val adb = sdkFile("platform-tools", platformExecutable(name = "adb"))

    private fun sdkFile(vararg path: String) =
        File(readAndroidSdkLocation(), path.joinToString(File.separator))

    fun platformExecutable(name: String, ext: String = "exe"): String =
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