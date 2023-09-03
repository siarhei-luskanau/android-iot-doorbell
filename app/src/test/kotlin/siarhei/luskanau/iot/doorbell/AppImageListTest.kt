package siarhei.luskanau.iot.doorbell

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.activityScenarioRule
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import siarhei.luskanau.iot.doorbell.navigation.NavRootDirections
import siarhei.luskanau.iot.doorbell.navigation.NavigationActivity
import kotlin.test.Test
import siarhei.luskanau.iot.doorbell.navigation.R as NavigationR
import siarhei.luskanau.iot.doorbell.ui.imagelist.R as ImageListR

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class AppImageListTest {

    @get:Rule
    val activityScenarioRule = activityScenarioRule<NavigationActivity>(
        intent = NavDeepLinkBuilder(ApplicationProvider.getApplicationContext())
            .setGraph(NavigationR.navigation.nav_app)
            .setDestination(ImageListR.id.nav_image_list_xml)
            .setArguments(
                NavRootDirections.actionDoorbellListToImageList(
                    doorbellId = "doorbellId",
                ).arguments,
            )
            .createTaskStackBuilder()
            .intents
            .first(),
    )

    @Test
    fun appTest() {
        activityScenarioRule.scenario.also { scenario ->
            scenario.moveToState(Lifecycle.State.RESUMED)
            scenario.onActivity { activity ->
                activity.findViewById<View>(android.R.id.content).getRootView().captureRoboImage(
                    filePath = "screenshots/AppImageList.png",
                )
            }
        }
    }
}
