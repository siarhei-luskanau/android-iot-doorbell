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
        rootProject.subprojects
            .filter { subProject ->
                listOf(
                    "src${File.separator}androidTest",
                    "src${File.separator}androidInstrumentedTest",
                )
                    .map { srcPath -> File(subProject.projectDir, srcPath) }
                    .any { srcDir -> srcDir.exists() }
            }
            .flatMap { subProject ->
                EMULATOR_VERSIONS.map { version ->
                    listOf(
                        "${subProject.path}:managedVirtualDevice${version}Check",
                        "-Pandroid.testoptions.manageddevices.emulator.gpu=swiftshader_indirect",
                    ).joinToString(separator = " ")
                }
            }
}
