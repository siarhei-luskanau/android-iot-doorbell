package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.common.test.ui.retryFlaky
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class AppKodeinDoorbellListTest {

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

            retryFlaky {
                Espresso.onView(ViewMatchers.withId(R.id.doorbellsRecyclerView))
                    .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))
            }

            activityScenario.onActivity {
                screenshotRule.captureScreenshot(
                    name = javaClass.simpleName + ".screenshot",
                    activity = it
                )
            }
        }
    }
}
