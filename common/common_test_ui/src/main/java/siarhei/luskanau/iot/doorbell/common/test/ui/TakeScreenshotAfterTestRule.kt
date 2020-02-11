package siarhei.luskanau.iot.doorbell.common.test.ui

import android.graphics.Bitmap
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.RuleChain
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import timber.log.Timber

class TakeScreenshotAfterTestRule : TestWatcher() {

    init {
        Screenshot.setScreenshotProcessors(
            setOf(androidx.test.runner.screenshot.ExternalFilesScreenCaptureProcessor())
        )
    }

    override fun succeeded(description: Description) {
        captureScreenshot(description.methodName)
    }

    override fun failed(e: Throwable?, description: Description) {
        captureScreenshot(description.methodName + "_fail")
    }

    companion object {

        fun captureScreenshot(name: String) {
            runCatching {
                val capture = Screenshot.capture()
                capture.format = Bitmap.CompressFormat.PNG
                capture.name = name
                capture.process()
            }.onFailure {
                Timber.e(it)
            }
        }

        fun screenshotRule(): RuleChain = RuleChain
            .outerRule(TakeScreenshotAfterTestRule())
    }
}
