import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestOptions
import org.gradle.kotlin.dsl.create

val EMULATOR_VERSIONS = 30..35

fun TestOptions.configureAndroidTestOptions() {
    unitTests {
        isIncludeAndroidResources = true
        all { test: org.gradle.api.tasks.testing.Test ->
            test.testLogging {
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                events = org.gradle.api.tasks.testing.logging.TestLogEvent.values().toSet()
            }
        }
    }
    animationsDisabled = true
    emulatorSnapshots {
        enableForTestFailures = false
    }
    EMULATOR_VERSIONS.forEach { version ->
        managedDevices.allDevices.create<ManagedVirtualDevice>("managedVirtualDevice$version") {
            device = "Pixel 2"
            apiLevel = version
            val systemImageConfig: Pair<String?, Boolean?> = when (apiLevel) {
                30, 33, 34, 35 -> "aosp" to true
                else -> null to null
            }
            systemImageConfig.first?.also { systemImageSource = it }
            systemImageConfig.second?.also { require64Bit = it }
        }
    }
}
