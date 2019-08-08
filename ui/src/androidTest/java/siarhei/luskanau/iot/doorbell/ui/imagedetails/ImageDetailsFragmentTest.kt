package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.junit.Test
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.R

class ImageDetailsFragmentTest {

    private fun createFragmentFactory(state: ImageDetailsState) = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            ImageDetailsFragment {
                object : ImageDetailsPresenter {
                    override fun getImageDetailsStateData(): LiveData<ImageDetailsState> =
                        MutableLiveData<ImageDetailsState>().apply { value = state }
                }
            }
    }

    @Test
    fun testNormalState() {
        val expectedImageData = ImageData(
            imageId = "expectedImageId",
            imageUri = "expectedImageUri",
            timestampString = "timestampString"
        )
        val fragmentFactory = createFragmentFactory(
            state = NormalImageDetailsState(imageData = expectedImageData)
        )
        val scenario = launchFragmentInContainer<ImageDetailsFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // error view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testErrorState() {
        val expectedErrorMessage = "Test Exception"
        val fragmentFactory = createFragmentFactory(
            state = ErrorImageDetailsState(
                error = RuntimeException(expectedErrorMessage)
            )
        )
        val scenario = launchFragmentInContainer<ImageDetailsFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(expectedErrorMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // normal view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.imageView))
            .check(ViewAssertions.doesNotExist())
    }
}
