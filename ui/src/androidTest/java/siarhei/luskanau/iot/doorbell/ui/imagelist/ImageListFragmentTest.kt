package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Matchers.not
import org.junit.Test
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.ui.R

class ImageListFragmentTest {

    private fun createFragmentFactory(state: ImageListState) = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            ImageListFragment {
                object : StubImageListPresenter() {
                    override fun getImageListStateData(): LiveData<ImageListState> =
                        MutableLiveData<ImageListState>().apply { value = state }
                }
            }
    }

    @Test
    fun testNormalStateAndIsAndroidThings() {
        val fragmentFactory = createFragmentFactory(
            state = NormalImageListState(
                cameraList = listOf(CameraData("NormalCameraId")),
                imageList = null,
                isAndroidThings = true
            )
        )
        val scenario = launchFragmentInContainer<ImageListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // reboot button should be visible
        Espresso.onView(ViewMatchers.withId(R.id.uptimeCardView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.rebootButton))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testNormalStateAndIsNotAndroidThings() {
        val fragmentFactory = createFragmentFactory(
            state = NormalImageListState(
                cameraList = listOf(CameraData("NormalCameraId")),
                imageList = null,
                isAndroidThings = false
            )
        )
        val scenario = launchFragmentInContainer<ImageListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // reboot button should be gone
        Espresso.onView(ViewMatchers.withId(R.id.uptimeCardView))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.rebootButton))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testEmptyState() {
        val fragmentFactory = createFragmentFactory(
            state = EmptyImageListState(
                cameraList = listOf(CameraData("EmptyCameraId")),
                isAndroidThings = false
            )
        )
        val scenario = launchFragmentInContainer<ImageListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // empty view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // reboot button should be gone
        Espresso.onView(ViewMatchers.withId(R.id.uptimeCardView))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.rebootButton))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testErrorState() {
        val expectedErrorMessage = "Test Exception"
        val fragmentFactory = createFragmentFactory(
            state = ErrorImageListState(
                error = RuntimeException(expectedErrorMessage)
            )
        )
        val scenario = launchFragmentInContainer<ImageListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(expectedErrorMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // reboot button should be gone
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.uptimeCardView))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.rebootButton))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.doesNotExist())
    }
}
