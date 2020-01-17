package siarhei.luskanau.iot.doorbell

import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class MainActivityTest {

    @get:Rule
    val intentsTestRule = IntentsTestRule(NavigationActivity::class.java)

    @Test
    fun someTest() {
    }
}
