package siarhei.luskanau.iot.doorbell.ui.splash

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import org.junit.Rule
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPreview
import kotlin.test.Test

class PermissionsFragmentTest : ScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testNormalState() {
        composeRule.setContent { PermissionsPreview() }
        compareScreenshot(
            rule = composeRule,
            name = javaClass.simpleName
        )
    }
}
