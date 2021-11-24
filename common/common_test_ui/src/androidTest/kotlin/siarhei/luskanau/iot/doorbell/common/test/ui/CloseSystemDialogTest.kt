package siarhei.luskanau.iot.doorbell.common.test.ui

import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import kotlin.test.Test
import org.junit.Rule

@LargeTest
class CloseSystemDialogTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule()

    @Test
    fun closeSystemDialogTest() {
        screenshotRule.captureScreenshot(name = javaClass.simpleName + ".before")
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val uiObject = uiDevice.findObject(UiSelector().text("Close app"))
        if (uiObject.exists()) {
            uiObject.click()
            uiDevice.waitForIdle(10_000)
            uiDevice.pressHome()
            uiDevice.waitForIdle(10_000)
        }
    }
}
