package siarhei.luskanau.iot.doorbell.common.test.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.view.View
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@SuppressLint("UnsafeOptInUsageError")
class TakeScreenshotAfterTestRule(
    private val onSucceeded: Boolean = true
) : TestWatcher() {

    init {
        Screenshot.setScreenshotProcessors(
            setOf(androidx.test.runner.screenshot.ExternalFilesScreenCaptureProcessor())
        )
    }

    override fun succeeded(description: Description) {
        if (onSucceeded) {
            captureScreenshot(description.testClass.simpleName + "." + description.methodName)
        }
    }

    override fun failed(e: Throwable?, description: Description) {
        captureScreenshot(description.testClass.simpleName + "." + description.methodName + "_fail")
    }

    fun captureScreenshot(
        name: String,
        view: View? = null,
        activity: Activity? = null
    ) {
        val capture = view?.let { Screenshot.capture(it) }
            ?: activity?.let { Screenshot.capture(it) }
            ?: run { Screenshot.capture() }
        capture.format = Bitmap.CompressFormat.PNG
        capture.name = name
        capture.process()
    }
}
