import com.google.gson.GsonBuilder
import org.gradle.api.Project
import java.io.File

class EmulatorJobsMatrix {

    private val gson by lazy {
        GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
    }

    fun createMatrixJsonFile(rootProject: Project) {
        val matrix = mapOf("gradle_tasks" to getTaskList(rootProject = rootProject))
        val jsonText = gson.toJson(matrix)
        rootProject.buildDir.mkdirs()
        File(rootProject.buildDir, "emulator_jobs_matrix.json").writeText(jsonText)
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

                    File(
                        subProject.projectDir,
                        "src${File.separator}androidInstrumentedTest"
                    ).exists() -> listOf("${subProject.path}:managedVirtualDevice${version}DebugAndroidTest")

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
                if (!true.toString().equals(other = System.getProperty("CI"), ignoreCase = true)) {
                    it.add("--enable-display")
                }
            }.joinToString(separator = " ")
        }.sorted()
}
