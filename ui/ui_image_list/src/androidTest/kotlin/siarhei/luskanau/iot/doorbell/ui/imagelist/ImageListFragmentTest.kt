package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import com.karumi.shot.ScreenshotTest
import kotlinx.coroutines.flow.flowOf
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import kotlin.test.Test
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
                    cameraList = listOf(CameraData("NormalCameraId"))
                )
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment {
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".normal"
            )
        }
    }

    @Test
    fun testEmptyState() {
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                state = ImageListState(
                    pagingData = PagingData.empty(),
                    cameraList = listOf(CameraData("EmptyCameraId"))
                )
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment {
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".empty"
            )
        }
    }
}
