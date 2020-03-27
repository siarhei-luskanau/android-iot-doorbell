package siarhei.luskanau.iot.doorbell.ui.doorbelllist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import siarhei.luskanau.iot.doorbell.common.test.ui.TakeScreenshotAfterTestRule
import siarhei.luskanau.iot.doorbell.data.model.CameraData

class DoorbellListFragmentTest {

    @get:Rule
    val screenshotRule: RuleChain = TakeScreenshotAfterTestRule.screenshotRule()

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
            state = NormalDoorbellListState(
                cameraList = listOf(CameraData("NormalCameraId")),
                doorbellList = null
            )
        )
        val scenario = launchFragmentInContainer<DoorbellListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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
            state = EmptyDoorbellListState(
                cameraList = listOf(CameraData("EmptyCameraId"))
            )
        )
        val scenario = launchFragmentInContainer<DoorbellListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // empty view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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
        val scenario = launchFragmentInContainer<DoorbellListFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(expectedErrorMessage))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // reboot button should be gone
        Espresso.onView(ViewMatchers.withId(R.id.camerasRecyclerView))
            .check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))

        // other views does not exist
        Espresso.onView(ViewMatchers.withId(R.id.doorbellsRecyclerView))
            .check(ViewAssertions.doesNotExist())
        Espresso.onView(ViewMatchers.withId(R.id.empty_message))
            .check(ViewAssertions.doesNotExist())
    }
}
