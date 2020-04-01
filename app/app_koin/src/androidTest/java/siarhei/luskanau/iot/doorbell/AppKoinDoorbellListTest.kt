package siarhei.luskanau.iot.doorbell

import android.content.Intent
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class AppKoinDoorbellListTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule(onSucceeded = false)

    @get:Rule
    val navigationRule =
        object : IntentsTestRule<NavigationActivity>(NavigationActivity::class.java) {
            override fun getActivityIntent(): Intent =
                NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
                    .setGraph(R.navigation.nav_app)
                    .setDestination(R.id.nav_doorbell_list_xml)
                    .setArguments(null)
                    .createTaskStackBuilder()
                    .intents
                    .first()
        }

    @Test
    fun appTest() {
        screenshotRule.captureScreenshot(
            name = javaClass.simpleName + ".screenshot",
            activity = navigationRule.activity
        )
    }
}
