package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import kotlin.test.Test
import org.junit.Rule

class SplashFragmentTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testNormalState() {
        composeRule.setContent { SplashPreview() }
        compareScreenshot(
            rule = composeRule,
            name = javaClass.simpleName
        )
    }
}
