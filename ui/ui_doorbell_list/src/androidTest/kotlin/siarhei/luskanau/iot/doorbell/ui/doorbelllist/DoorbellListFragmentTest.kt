package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import com.karumi.shot.ScreenshotTest
import kotlinx.coroutines.flow.flowOf
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import kotlin.test.Test
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR

class DoorbellListFragmentTest : ScreenshotTest {

    private fun createFragment(pagingData: PagingData<DoorbellData>) = DoorbellListFragment {
        mock(DoorbellListPresenter::class.java).apply {
            given(doorbellListFlow).willReturn(flowOf(pagingData))
        }
    }

    @Test
    fun testNormalState() {
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(PagingData.from(listOf(DoorbellData(doorbellId = "doorbellId"))))
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
            createFragment(PagingData.empty())
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
