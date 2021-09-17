package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.common.test.ui.retryFlaky
import siarhei.luskanau.iot.doorbell.navigation.NavRootDirections
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import siarhei.luskanau.iot.doorbell.navigation.R as NavigationR
import siarhei.luskanau.iot.doorbell.ui.imagelist.R as ImageListR

@LargeTest
class AppKoinImageListTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule(onSucceeded = false)

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(NavigationR.navigation.nav_app)
            .setDestination(ImageListR.id.nav_image_list_xml)
            .setArguments(
                NavRootDirections.actionDoorbellListToImageList(
                    doorbellId = "doorbellId"
                ).arguments
            )
            .createTaskStackBuilder()
            .intents
            .first()
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)

        retryFlaky {
            onView(withId(ImageListR.id.imagesRecyclerView))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        }

        activityScenarioRule.scenario.onActivity {
            screenshotRule.captureScreenshot(
                name = javaClass.simpleName + ".screenshot",
                activity = it
            )
        }
    }
}
