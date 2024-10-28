import com.google.gson.GsonBuilder
import org.gradle.api.Project
import java.io.File
import java.util.Properties

class EmulatorJobsMatrix {

    private val gson by lazy {
        GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
    }

    fun createMatrixJsonFile(rootProject: Project) {
        val map = getTaskList(rootProject = rootProject).map {
            mapOf(
                "experimental" to it.contains("managedVirtualDevice35"),
                "gradle_tasks" to it
            )
        }
        val matrix = mapOf("variants" to map)
        val jsonText = gson.toJson(matrix)
        rootProject.layout.buildDirectory.asFile.get().mkdirs()
        File(rootProject.layout.buildDirectory.asFile.get(), "emulator_jobs_matrix.json").writeText(jsonText)
    }

    fun getTaskList(rootProject: Project): List<String> =
        rootProject.subprojects.flatMap { subProject ->
            EMULATOR_VERSIONS.mapNotNull { version ->
                when {
                    ":app" == subProject.path -> listOf(
                        "${subProject.path}:managedVirtualDevice${version}DiDaggerDebugAndroidTest",
                        "${subProject.path}:managedVirtualDevice${version}DiKodeinDebugAndroidTest",
                        "${subProject.path}:managedVirtualDevice${version}DiKoinDebugAndroidTest",
                        "${subProject.path}:managedVirtualDevice${version}DiManualDebugAndroidTest",
                    )

                    File(subProject.projectDir, "src${File.separator}androidInstrumentedTest").exists() -> listOf(
                        "${subProject.path}:managedVirtualDevice${version}DebugAndroidTest"
                    )

                    else -> null
                }
            }
        }.map { taskList ->
            taskList.toMutableList().also {
                it.add("--no-parallel")
                it.add("--max-workers=1")
                it.add("-Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect")
                it.add("-Pandroid.experimental.testOptions.managedDevices.emulator.showKernelLogging=true")
            }.also {
                if (!true.toString().equals(other = System.getenv("CI"), ignoreCase = true)) {
                    it.add("--enable-display")
                }
            }.joinToString(separator = " ")
        }.sorted()
}

fun getAndroidSdkPath(rootDir: File): String? = Properties().apply {
    val propertiesFile = File(rootDir, "local.properties")
    if (propertiesFile.exists()) {
        load(propertiesFile.inputStream())
    }
}.getProperty("sdk.dir").let { propertiesSdkDirPath ->
    (propertiesSdkDirPath ?: System.getenv("ANDROID_HOME"))
}

fun getApksignerFile(rootDir: File): File? = getAndroidSdkPath(rootDir = rootDir)?.let { sdkDirPath ->
    println("sdkDirPath: $sdkDirPath")
    val apksignerFile = File(sdkDirPath).walk()
        .filter { it.path.endsWith("apksigner") }
        .map {
            println("walk apksigner: ${it.absolutePath}")
            it
        }
        .firstOrNull()
    println("apksignerFile: $apksignerFile")
    apksignerFile
}

fun getSdkmanagerFile(rootDir: File): File? = getAndroidSdkPath(rootDir = rootDir)?.let { sdkDirPath ->
    println("sdkDirPath: $sdkDirPath")
    val files = File(sdkDirPath).walk().filter { file ->
        file.path.contains("cmdline-tools") && file.path.endsWith("sdkmanager")
    }
    files.forEach { println("walk: ${it.absolutePath}") }
    val sdkmanagerFile = files.firstOrNull()
    println("sdkmanagerFile: $sdkmanagerFile")
    sdkmanagerFile
}
