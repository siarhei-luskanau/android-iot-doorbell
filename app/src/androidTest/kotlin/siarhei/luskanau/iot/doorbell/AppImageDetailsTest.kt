package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import siarhei.luskanau.iot.doorbell.navigation.NavRootDirections
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import kotlin.test.Test
import siarhei.luskanau.iot.doorbell.navigation.R as NavigationR
import siarhei.luskanau.iot.doorbell.ui.imagedetails.R as ImageDetailsR

@LargeTest
class AppImageDetailsTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(NavigationR.navigation.nav_app)
            .setDestination(ImageDetailsR.id.nav_image_details_xml)
            .setArguments(
                NavRootDirections.actionImageListToImageDetails(
                    doorbellId = "doorbellId",
                    imageId = "1",
                ).arguments,
            )
            .createTaskStackBuilder()
            .intents
            .first(),
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
    }
}
