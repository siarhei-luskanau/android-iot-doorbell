package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.karumi.shot.ScreenshotTest
import kotlin.test.Test
import kotlinx.coroutines.flow.flowOf
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR

class ImageListFragmentTest : ScreenshotTest {

    private fun createFragment(state: ImageListState) = ImageListFragment {
        mock(ImageListPresenter::class.java).apply {
            given(viewStateFlow).willReturn(flowOf(state))
        }
    }

    @Test
    fun testNormalState() {
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                state = ImageListState(
                    pagingData = PagingData.from(listOf(ImageData(imageId = "imageId"))),
                    cameraList = listOf(CameraData("NormalCameraId")),
                )
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(CommonR.id.empty_message))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(CommonR.id.error_message))
            .check(ViewAssertions.doesNotExist())

        scenario.onFragment {
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".normal",
            )
        }
    }

    @Test
    fun testEmptyState() {
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                state = ImageListState(
                    pagingData = PagingData.empty(),
                    cameraList = listOf(CameraData("EmptyCameraId")),
                )
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        // empty view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(CommonR.id.empty_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.imagesRecyclerView))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(CommonR.id.error_message))
            .check(ViewAssertions.doesNotExist())

        scenario.onFragment {
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".empty",
            )
        }
    }
}
