package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.paging.PagingData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule

class DoorbellListFragmentTest {

    @get:Rule
    val screenshotRule = TakeScreenshotAfterTestRule()

    private fun createFragmentFactory(state: DoorbellListState) = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            DoorbellListFragment {
                object : StubDoorbellListPresenter() {
                    override fun getDoorbellListFlow(): Flow<DoorbellListState> = flowOf(state)
                }
            }
    }

    @Test
    fun testNormalState() {
        val fragmentFactory = createFragmentFactory(
            state = NormalDoorbellListState(pagingData = PagingData.empty())
        )
        launchFragmentInContainer<DoorbellListFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        ).apply {
            moveToState(Lifecycle.State.RESUMED)
        }

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.doorbellsRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testEmptyState() {
        val fragmentFactory = createFragmentFactory(
            state = EmptyDoorbellListState
        )
        launchFragmentInContainer<DoorbellListFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        ).apply {
            moveToState(Lifecycle.State.RESUMED)
        }

        // empty view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.doorbellsRecyclerView))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testErrorState() {
        val expectedErrorMessage = "Test Exception"
        val fragmentFactory = createFragmentFactory(
            state = ErrorDoorbellListState(
                error = RuntimeException(expectedErrorMessage)
            )
        )
        launchFragmentInContainer<DoorbellListFragment>(
            factory = fragmentFactory,
            themeResId = R.style.AppTheme
        ).apply {
            moveToState(Lifecycle.State.RESUMED)
        }

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(expectedErrorMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.doorbellsRecyclerView))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.doesNotExist())
    }
}
