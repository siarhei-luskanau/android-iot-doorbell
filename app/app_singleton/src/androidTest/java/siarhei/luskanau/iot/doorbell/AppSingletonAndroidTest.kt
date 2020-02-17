package siarhei.luskanau.iot.doorbell

import android.Manifest
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class AppSingletonAndroidTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule()

    @get:Rule
    val grantPermissionRule: GrantPermissionRule? =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>()

    @Test
    fun appTest() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.STARTED)
    }
}
