package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import kotlin.test.Test
import org.junit.Rule
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPreview

class PermissionsFragmentTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testNormalState() {
        composeRule.setContent { PermissionsPreview() }
        compareScreenshot(
            rule = composeRule,
            name = javaClass.simpleName,
        )
    }
}
