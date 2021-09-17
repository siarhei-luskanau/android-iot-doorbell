package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR
import siarhei.luskanau.iot.doorbell.ui.imagedetails.R

class ImageDetailsSlideFragmentTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule()

    private fun createFragment(state: ImageDetailsSlideState) = ImageDetailsSlideFragment {
        mock(ImageDetailsSlidePresenter::class.java).apply {
            given(getImageDetailsSlideStateFlow()).willReturn(flowOf(state))
        }
    }

    @Test
    fun testNormalState() {
        val expectedImageData = ImageData(
            imageId = "expectedImageId",
            imageUri = "expectedImageUri",
            timestampString = "timestampString"
        )
        launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(state = NormalImageDetailsSlideState(imageData = expectedImageData))
        }.apply {
            moveToState(Lifecycle.State.RESUMED)
        }

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // error view does not exist
        Espresso.onView(ViewMatchers.withId(CommonR.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testErrorState() {
        val expectedErrorMessage = "Test Exception"
        launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                state = ErrorImageDetailsSlideState(error = RuntimeException(expectedErrorMessage))
            )
        }.apply {
            moveToState(Lifecycle.State.RESUMED)
        }

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(CommonR.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(expectedErrorMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // normal view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.doesNotExist())
    }
}
