package androidx.test.runner.screenshot

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@SuppressLint("UnsafeOptInUsageError")
class ExternalFilesScreenCaptureProcessor(
    private val destPath: String = SCREENSHOTS_PATH,
    defaultScreenshotPath: File = File(
        ApplicationProvider.getApplicationContext<Context>()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "screenshots"
    )
) : BasicScreenCaptureProcessor(defaultScreenshotPath) {

    override fun process(capture: ScreenCapture?): String {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val uiAutomation = instrumentation.uiAutomation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val packageName = instrumentation.context.packageName
            val targetPackageName = instrumentation.targetContext.packageName
            STORAGE_PERMISSIONS.forEach { permission ->
                uiAutomation.executeShellCommand("pm grant $packageName $permission")
                uiAutomation.executeShellCommand("pm grant $targetPackageName $permission")
            }
        }

        val filename = super.process(capture)
        val imageFile = File(mDefaultScreenshotPath, filename)

        uiAutomation.executeShellCommand("mkdir -p $destPath")
        uiAutomation.executeShellCommand("cp ${imageFile.absolutePath} $destPath")

        return filename
    }

    override fun getFilename(prefix: String?): String {
        val instant = Clock.System.now()
        val localDateTime: LocalDateTime = instant.toLocalDateTime(TimeZone.UTC)
        val formatted = StringBuilder()
            .append(localDateTime.year).append(".")
            .append(localDateTime.monthNumber).append(".")
            .append(localDateTime.dayOfMonth).append("-")
            .append(localDateTime.hour).append(".")
            .append(localDateTime.minute).append(".")
            .append(localDateTime.second).append(".")
            .append(localDateTime.nanosecond.toString().take(3))
            .toString()
        return prefix + mFileNameDelimiter + formatted
    }

    companion object {
        @SuppressLint("SdCardPath")
        private val SCREENSHOTS_PATH = "/sdcard/Pictures/screenshots/"
        private val STORAGE_PERMISSIONS = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    }
}
