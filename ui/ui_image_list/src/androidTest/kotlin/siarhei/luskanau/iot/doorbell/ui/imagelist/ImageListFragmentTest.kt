package siarhei.luskanau.iot.doorbell.ui.imagelist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import com.karumi.shot.ScreenshotTest
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR

class ImageListFragmentTest : ScreenshotTest {

    private fun createFragment(
        pagingData: PagingData<ImageData>,
        cameraList: List<CameraData>
    ) = ImageListFragment {
        mockk(relaxed = true, relaxUnitFun = true) {
            every { doorbellListFlow } returns flowOf(pagingData)
            every { runBlocking { getCameraList() } } returns cameraList
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
                            imageUri = null
                        )
                    )
                ),
                cameraList = listOf(CameraData("NormalCameraId"))
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
                pagingData = PagingData.empty(),
                cameraList = listOf(CameraData("EmptyCameraId"))
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
