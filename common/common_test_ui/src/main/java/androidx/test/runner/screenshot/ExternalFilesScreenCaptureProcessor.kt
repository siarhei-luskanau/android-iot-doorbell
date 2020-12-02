package androidx.test.runner.screenshot

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

class ExternalFilesScreenCaptureProcessor(
    private val destPath: String = SCREENSHOTS_PATH,
    defaultScreenshotPath: File = File(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ApplicationProvider.getApplicationContext<Context>().cacheDir
        } else {
            ApplicationProvider.getApplicationContext<Context>()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        },
        "screenshots"
    )
) : BasicScreenCaptureProcessor(defaultScreenshotPath) {

    override fun process(capture: ScreenCapture?): String {
        val filename = super.process(capture)
        val imageFile = File(mDefaultScreenshotPath, filename)

        InstrumentationRegistry.getInstrumentation().uiAutomation?.let {
            it.executeShellCommand("mkdir -p $destPath")
            it.executeShellCommand("cp ${imageFile.absolutePath} $destPath")
        }

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
    }
}
