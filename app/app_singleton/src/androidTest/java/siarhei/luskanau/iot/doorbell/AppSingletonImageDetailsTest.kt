package siarhei.luskanau.iot.doorbell

import android.content.Intent
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.navigation.NavRootDirections
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity

@LargeTest
class AppSingletonImageDetailsTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule(onSucceeded = false)

    @get:Rule
    val navigationRule =
        object : IntentsTestRule<NavigationActivity>(NavigationActivity::class.java) {
            override fun getActivityIntent(): Intent =
                NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
                    .setGraph(R.navigation.nav_app)
                    .setDestination(R.id.nav_image_details_xml)
                    .setArguments(
                        NavRootDirections.actionImageListToImageDetails(
                            doorbellData = DoorbellData(doorbellId = "doorbellId"),
                            imageData = ImageData(imageId = "imageId")
                        ).arguments
                    )
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
