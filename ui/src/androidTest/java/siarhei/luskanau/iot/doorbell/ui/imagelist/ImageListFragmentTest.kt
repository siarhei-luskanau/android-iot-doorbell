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
import org.junit.Test
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.ui.R
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment

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
    fun testNormalState() {
        val fragmentFactory = createFragmentFactory(
            state = NormalImageListState(
                cameraList = listOf(CameraData("cameraId")),
                imageList = null
            )
        )
        val scenario = launchFragmentInContainer<ImageDetailsFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.uptimeCardView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // error view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }
}