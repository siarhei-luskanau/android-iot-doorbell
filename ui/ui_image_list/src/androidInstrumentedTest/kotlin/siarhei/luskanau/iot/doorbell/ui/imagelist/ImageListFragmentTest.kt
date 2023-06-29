package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import com.karumi.shot.ScreenshotTest
import kotlinx.coroutines.flow.flowOf
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import kotlin.test.Test
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR

class ImageListFragmentTest : ScreenshotTest {

    private fun createFragment(
        pagingData: PagingData<ImageData>,
        cameraList: List<CameraData>,
    ) = ImageListFragment {
        object : ImageListPresenter {
            override suspend fun getCameraList(): List<CameraData> = cameraList
            override val doorbellListFlow = flowOf(pagingData)
            override fun onCameraClicked(cameraData: CameraData) = Unit
            override fun onImageClicked(imageData: ImageData) = Unit
        }
    }

    @Test
    fun testNormalState() {
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(
                pagingData = PagingData.from(
                    listOf(
                        ImageData(
                            imageId = "1",
                            imageUri = null,
                        ),
                    ),
                ),
                cameraList = listOf(CameraData("NormalCameraId")),
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)
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
                pagingData = PagingData.empty(),
                cameraList = listOf(CameraData("EmptyCameraId")),
            )
        }
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment {
            compareScreenshot(
                fragment = it,
                name = javaClass.simpleName + ".empty",
            )
        }
    }
}
