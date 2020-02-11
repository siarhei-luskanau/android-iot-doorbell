package androidx.test.runner.screenshot

import android.content.Context
import android.os.Environment
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.text.DecimalFormat

class ExternalFilesScreenCaptureProcessor(
    private val destPath: String = "/sdcard/Pictures/screenshots/",
    defaultScreenshotPath: File = File(
        ApplicationProvider.getApplicationContext<Context>()
            .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "screenshots"
    )
) : BasicScreenCaptureProcessor(defaultScreenshotPath) {

    private var counter = 1
    private val decimalFormat = DecimalFormat("00")

    override fun process(capture: ScreenCapture?): String {
        val filename = super.process(capture)
        val imageFile = File(mDefaultScreenshotPath, filename)

        InstrumentationRegistry.getInstrumentation().uiAutomation?.let {
            it.executeShellCommand("mkdir -p $destPath")
            it.executeShellCommand("cp ${imageFile.absolutePath} $destPath")
        }

        return filename
    }

    override fun getFilename(prefix: String?): String =
        super.getFilename(prefix + mFileNameDelimiter + decimalFormat.format(counter))
            .also { counter += 1 }
}
