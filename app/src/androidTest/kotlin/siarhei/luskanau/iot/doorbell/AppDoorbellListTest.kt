package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import kotlin.test.Test
import siarhei.luskanau.iot.doorbell.navigation.R as NavigationR
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.R as DoorbellListR

@LargeTest
class AppDoorbellListTest {

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA,
    )

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(NavigationR.navigation.nav_app)
            .setDestination(DoorbellListR.id.nav_doorbell_list_xml)
            .setArguments(null)
            .createTaskStackBuilder()
            .intents
            .first(),
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
    }
}
