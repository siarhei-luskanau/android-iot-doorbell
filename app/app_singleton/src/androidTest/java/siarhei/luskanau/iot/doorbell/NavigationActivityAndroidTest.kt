package siarhei.luskanau.iot.doorbell

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class NavigationActivityAndroidTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(NavigationActivity::class.java)

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>()

    @Test
    fun someTest() {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
    }
}
