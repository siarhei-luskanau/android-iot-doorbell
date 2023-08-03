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
val COMMANDLINETOOLS_VERSION = "9477386"
val COMMANDLINETOOLS_LINUX =
    "https://dl.google.com/android/repository/commandlinetools-linux-" +
        "${COMMANDLINETOOLS_VERSION}_latest.zip"
val COMMANDLINETOOLS_MAC =
    "https://dl.google.com/android/repository/commandlinetools-mac-" +
        "${COMMANDLINETOOLS_VERSION}_latest.zip"
val COMMANDLINETOOLS_WIN =
    "https://dl.google.com/android/repository/commandlinetools-win-" +
        "${COMMANDLINETOOLS_VERSION}_latest.zip"
val CMDLINE_TOOLS_VERSION = "11.0"

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

        runExec(
            commands = listOf(
                "${androidSdkConfig.sdkmanager}",
                "--licenses",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            ),
            inputText = YES_INPUT,
        )

        runExec(
            commands = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "cmdline-tools;$CMDLINE_TOOLS_VERSION",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            ),
            inputText = YES_INPUT,
        )
    }
}

tasks.register("setupAndroidSDK") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        runExec(
            commands = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "--update",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            ),
            inputText = YES_INPUT,
        )

        runExec(
            commands = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "--licenses",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            ),
            inputText = YES_INPUT,
        )

        runExec(
            commands = mutableListOf(
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
            },
            inputText = YES_INPUT,
        )

        runExec(
            commands = listOf(
                androidSdkConfig.sdkmanager.absolutePath,
                "--list",
                "--sdk_root=${androidSdkConfig.sdkDirPath}",
            ),
        )
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
                runExec(
                    commands = listOf(
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
                    ),
                )
            }

        runExec(commands = listOf(androidSdkConfig.avdmanager.absolutePath, "-v", "list", "avd"))
    }
}

tasks.register("runAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        runExec(commands = listOf(androidSdkConfig.adb.absolutePath, "start-server"))

        val avdName = System.getProperty(GradleArguments.EMULATOR_AVD_NAME).orEmpty()
            .also { println("System.getProperty(${GradleArguments.EMULATOR_AVD_NAME}): $it") }
        val emulatorConfig = requireNotNull(ANDROID_EMULATORS.find { it.avdName == avdName })
            .also { println("EmulatorConfig: $it") }

        // https://developer.android.com/studio/run/emulator-commandline#startup-options
        val commandArgs = listOf(
            androidSdkConfig.emulator.absolutePath,
            "-avd",
            emulatorConfig.avdName,
            "-port",
            emulatorConfig.port,
            "-accel",
            "auto",
            "-gpu",
            "auto",
            "-no-snapshot",
            "-no-audio",
            "-no-boot-anim",
            // "-no-window",
        )

        runExec(commands = commandArgs)
    }
}

tasks.register("fixAndroidEmulatorSize") {
    group = EMULATOR_GRADLE
    doLast {
        val avdName = requireNotNull(System.getProperty(GradleArguments.EMULATOR_AVD_NAME)) {
            "Please provide EMULATOR_AVD_NAME argument"
        }
        val emulatorPath: String = runExec(
            commands = listOf(androidSdkConfig.avdmanager.absolutePath, "list", "avd"),
        )
            .split("---------")
            .first { it.contains(avdName) }
            .lines()
            .filter { it.contains("Path:") }
            .map { it.substringAfter("Path:") }
            .first()
            .trim()

        val userdataQemuImgFile = File(File(emulatorPath), "userdata-qemu.img")
        while (!userdataQemuImgFile.exists()) {
            println("Wait for file: ${userdataQemuImgFile.name}")
            Thread.sleep(500)
        }
        Thread.sleep(500)
        runCatching {
            runExec(
                commands = listOf(
                    androidSdkConfig.resize2fs.absolutePath,
                    userdataQemuImgFile.path,
                    "1024M",
                ),
            )
        }.onFailure { it.printStackTrace() }
        println("${userdataQemuImgFile.name} is resized")
    }
}

tasks.register("waitAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        var isEmulatorFound = false
        for (i in 1..700) {
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
                        result = runCatching {
                            runExec(
                                commands = listOf(
                                    androidSdkConfig.adb.absolutePath,
                                    "-s",
                                    emulatorAttributes.first(),
                                    "wait-for-device",
                                    "shell",
                                    "getprop sys.boot_completed",
                                ),
                            )
                        }.getOrNull()
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
            runExec(
                commands = listOf(
                    androidSdkConfig.adb.absolutePath,
                    "-s",
                    emulatorAttributes.first(),
                    "emu",
                    "kill",
                ),
            )
        }
    }
}

tasks.register("deleteAndroidEmulator") {
    group = EMULATOR_GRADLE
    doLast {
        androidSdkConfig.printSdkPath()
        ANDROID_EMULATORS.forEach { emulatorConfig ->
            runCatching {
                runExec(
                    commands = listOf(
                        androidSdkConfig.avdmanager.absolutePath,
                        "-v",
                        "delete",
                        "avd",
                        "-n",
                        emulatorConfig.avdName,
                    ),
                )
            }
        }

        runExec(commands = listOf(androidSdkConfig.avdmanager.absolutePath, "-v", "list", "avd"))
    }
}

fun runExec(
    commands: List<String>,
    inputText: String? = null,
    isOutputPrinted: Boolean = true,
): String =
    ByteArrayOutputStream().let { resultOutputStream ->
        exec {
            commandLine = commands
            inputText?.also { standardInput = it.byteInputStream() }
            standardOutput = resultOutputStream
            println("commandLine: ${this.commandLine.joinToString(separator = " ")}")
        }.apply { println("ExecResult: $this") }
        String(resultOutputStream.toByteArray()).trim()
    }.also {
        if (isOutputPrinted) {
            println(it)
        }
    }

class AndroidSdkConfig {

    val sdkDirPath: String by lazy {
        readAndroidSdkFromLocalProperties()
            ?: System.getenv("ANDROID_HOME")
            ?: System.getenv("sdk.dir")
            ?: listOfNotNull(
                System.getProperty("user.home"),
                if (Os.isFamily(Os.FAMILY_MAC)) "Library" else null,
                "Android",
                if (Os.isFamily(Os.FAMILY_MAC)) "sdk" else "Sdk",
            ).joinToString(separator = File.separator)
    }

    init {
        writeAndroidSdkToLocalProperties()
    }

    val sdkmanager = sdkFile(
        "cmdline-tools",
        "bin",
        platformExecutable(name = "sdkmanager", ext = "bat"),
    )
    val avdmanager = sdkFile(
        "cmdline-tools",
        CMDLINE_TOOLS_VERSION,
        "bin",
        platformExecutable(name = "avdmanager", ext = "bat"),
    )
    val resize2fs = sdkFile(
        "emulator",
        "bin64",
        platformExecutable(name = "resize2fs", ext = "bat"),
    )
    val emulator = sdkFile("emulator", platformExecutable(name = "emulator"))
    val adb = sdkFile("platform-tools", platformExecutable(name = "adb"))

    fun getDevicesList(): List<List<String>> =
        runExec(commands = listOf(adb.absolutePath, "devices", "-l")).let { devicesOutput ->
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
        println("resize2fs: ${resize2fs.exists()}: $resize2fs")
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
        private fun platformExecutable(name: String, ext: String = "exe"): String =
            if (Os.isFamily(Os.FAMILY_WINDOWS)) {
                "$name.$ext"
            } else {
                name
            }
    }
}
