package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.navigation.NavRootDirections
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import siarhei.luskanau.iot.doorbell.navigation.R as NavigationR
import siarhei.luskanau.iot.doorbell.ui.imagedetails.R as ImageDetailsR

@LargeTest
class AppKodeinImageDetailsTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule(onSucceeded = false)

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(NavigationR.navigation.nav_app)
            .setDestination(ImageDetailsR.id.nav_image_details_xml)
            .setArguments(
                NavRootDirections.actionImageListToImageDetails(
                    doorbellId = "doorbellId",
                    imageId = "imageId"
                ).arguments
            )
            .createTaskStackBuilder()
            .intents
            .first()
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
        activityScenarioRule.scenario.onActivity {
            screenshotRule.captureScreenshot(
                name = javaClass.simpleName + ".screenshot",
                activity = it
            )
        }
    }
}
