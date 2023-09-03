package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.flow.flowOf
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.imagedetails.R
import kotlin.test.Test
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [33])
class ImageDetailsSlideFragmentTest {
    private fun createFragment(state: ImageDetailsSlideState) = ImageDetailsSlideFragment {
        object : ImageDetailsSlidePresenter {
            override fun getImageDetailsSlideStateFlow() = flowOf(state)
        }
    }

    @Test
    fun testNormalState() {
        val expectedImageData = ImageData(
            imageId = "expectedImageId",
            imageUri = "expectedImageUri",
            timestampString = "timestampString",
        )
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(state = NormalImageDetailsSlideState(imageData = expectedImageData))
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // error view does not exist
        Espresso.onView(ViewMatchers.withId(CommonR.id.error_message))
            .check(ViewAssertions.doesNotExist())

        scenario.onFragment {
            it.view?.captureRoboImage(filePath = "screenshots/ImageDetailsSlideFragment.normal.png")
        }
    }

    @Test
    fun testErrorState() {
        val expectedErrorMessage = "Test Exception"
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                state = ErrorImageDetailsSlideState(error = RuntimeException(expectedErrorMessage)),
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(CommonR.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(expectedErrorMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // normal view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.doesNotExist())

        scenario.onFragment {
            it.view?.captureRoboImage(filePath = "screenshots/ImageDetailsSlideFragment.empty.png")
        }
    }
}
