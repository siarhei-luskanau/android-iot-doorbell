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
import androidx.test.rule.GrantPermissionRule
import kotlin.test.Test
import org.junit.Rule
import siarhei.luskanau.iot.doorbell.common.test.ui.retryFlaky
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import siarhei.luskanau.iot.doorbell.navigation.R as NavigationR
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.R as DoorbellListR

@LargeTest
class AppSingletonDoorbellListTest {

    @get:Rule
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.CAMERA
    )

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(NavigationR.navigation.nav_app)
            .setDestination(DoorbellListR.id.nav_doorbell_list_xml)
            .setArguments(null)
            .createTaskStackBuilder()
            .intents
            .first()
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)

        retryFlaky {
            onView(withId(DoorbellListR.id.doorbellsRecyclerView))
                .perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        }
    }
}
