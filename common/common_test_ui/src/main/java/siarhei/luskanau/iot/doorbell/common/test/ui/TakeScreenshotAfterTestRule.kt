package siarhei.luskanau.iot.doorbell.common.test.ui

import android.graphics.Bitmap
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TakeScreenshotAfterTestRule : TestWatcher() {

    init {
        Screenshot.setScreenshotProcessors(
            setOf(androidx.test.runner.screenshot.ExternalFilesScreenCaptureProcessor())
        )
    }

    override fun succeeded(description: Description) {
        captureScreenshot(description.testClass.simpleName + "." + description.methodName)
    }

    override fun failed(e: Throwable?, description: Description) {
        captureScreenshot(description.testClass.simpleName + "." + description.methodName + "_fail")
    }

    private fun captureScreenshot(name: String) {
        val capture = Screenshot.capture()
        capture.format = Bitmap.CompressFormat.PNG
        capture.name = name
        capture.process()
    }
}
