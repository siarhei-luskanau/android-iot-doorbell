package androidx.test.runner.screenshot

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getFilename(prefix: String?): String =
        prefix + mFileNameDelimiter + FORMATTER.format(LocalDateTime.now())

    companion object {
        @SuppressLint("SdCardPath")
        private val SCREENSHOTS_PATH = "/sdcard/Pictures/screenshots/"

        @RequiresApi(Build.VERSION_CODES.O)
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss.S")
    }
}
