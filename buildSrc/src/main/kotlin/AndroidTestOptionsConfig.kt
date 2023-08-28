import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestOptions
import org.gradle.kotlin.dsl.create

val EMULATOR_VERSIONS = 30..34

fun TestOptions.configureAndroidTestOptions() {
    unitTests {
        all { test: org.gradle.api.tasks.testing.Test ->
            test.testLogging.events = setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT,
                org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR,
            )
        }
    }
    animationsDisabled = true
    emulatorSnapshots {
        enableForTestFailures = false
    }
    EMULATOR_VERSIONS.forEach { version ->
        managedDevices.devices.create<ManagedVirtualDevice>("managedVirtualDevice$version") {
            device = "Pixel 2"
            apiLevel = version
        }
    }
}
