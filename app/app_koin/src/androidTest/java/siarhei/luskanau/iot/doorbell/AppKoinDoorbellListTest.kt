package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class AppKoinDoorbellListTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule(onSucceeded = false)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule? =
        GrantPermissionRule.grant(android.Manifest.permission.CAMERA)

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(R.navigation.nav_app)
            .setDestination(R.id.nav_doorbell_list_xml)
            .setArguments(null)
            .createTaskStackBuilder()
            .intents
            .first()
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.let { activityScenario ->
            activityScenario.moveToState(Lifecycle.State.RESUMED)
            activityScenario.onActivity {
                screenshotRule.captureScreenshot(
                    name = javaClass.simpleName + ".screenshot",
                    activity = it
                )
            }
        }
    }
}
