package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.karumi.shot.ScreenshotTest
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlinx.coroutines.flow.flowOf
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR
import siarhei.luskanau.iot.doorbell.ui.imagedetails.R

class ImageDetailsSlideFragmentTest : ScreenshotTest {

    private fun createFragment(state: ImageDetailsSlideState) = ImageDetailsSlideFragment {
        mockk(relaxed = true, relaxUnitFun = true) {
            every { getImageDetailsSlideStateFlow() } returns flowOf(state)
        }
    }

    @Test
    fun testNormalState() {
        val expectedImageData = ImageData(
            imageId = "expectedImageId",
            imageUri = "expectedImageUri",
            timestampString = "timestampString"
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
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".normal"
            )
        }
    }

    @Test
    fun testErrorState() {
        val expectedErrorMessage = "Test Exception"
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                state = ErrorImageDetailsSlideState(error = RuntimeException(expectedErrorMessage))
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
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".empty"
            )
        }
    }
}
