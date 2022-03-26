package siarhei.luskanau.iot.doorbell.ui.doorbelllist

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
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.ui.common.R as CommonR
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.R as DoorbellListR

class DoorbellListFragmentTest : ScreenshotTest {

    private fun createFragment(list: List<DoorbellData>) = DoorbellListFragment {
        mock(DoorbellListPresenter::class.java).apply {
            given(doorbellListFlow).willReturn(flowOf(PagingData.from(list)))
        }
    }

    @Test
    fun testNormalState() {
        val scenario = launchFragmentInContainer(themeResId = CommonR.style.AppTheme) {
            createFragment(list = listOf(DoorbellData(doorbellId = "doorbellId")))
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(DoorbellListR.id.doorbellsRecyclerView))
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
            createFragment(list = emptyList())
        }
        scenario.moveToState(Lifecycle.State.RESUMED)

        // empty view is displayed
        Espresso.onView(ViewMatchers.withId(CommonR.id.empty_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(DoorbellListR.id.doorbellsRecyclerView))
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
