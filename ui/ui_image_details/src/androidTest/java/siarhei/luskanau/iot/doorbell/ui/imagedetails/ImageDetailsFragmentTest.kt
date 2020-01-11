package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.junit.Test

class ImageDetailsFragmentTest {

    companion object {
        const val EXPECTED_ERROR_MESSAGE = "Test Exception"
    }

    private fun createNormalFragmentFactory() = object : FragmentFactory() {
        override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
            ImageDetailsFragment { fragment: Fragment ->
                object : ImageDetailsPresenter {
                    override fun getImageDetailsStateData(): LiveData<ImageDetailsState> =
                        MutableLiveData<ImageDetailsState>().apply {
                            value = NormalImageDetailsState(
                                adapter = object : FragmentStateAdapter(fragment) {
                                    override fun getItemCount(): Int = 1
                                    override fun createFragment(position: Int): Fragment =
                                        Fragment(R.layout.layout_generic_empty)
                                }
                            )
                        }
                }
            }
    }

    private fun createErrorFragmentFactory() =
        object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment =
                ImageDetailsFragment {
                    object : ImageDetailsPresenter {
                        override fun getImageDetailsStateData(): LiveData<ImageDetailsState> =
                            MutableLiveData<ImageDetailsState>().apply {
                                value = ErrorImageDetailsState(
                                    error = RuntimeException(EXPECTED_ERROR_MESSAGE)
                                )
                            }
                    }
                }
        }

    @Test
    fun testNormalState() {
        val fragmentFactory = createNormalFragmentFactory()
        val scenario = launchFragmentInContainer<ImageDetailsFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // normal view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.viewPager2))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // error view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.doesNotExist())
    }

    @Test
    fun testErrorState() {
        val fragmentFactory = createErrorFragmentFactory()
        val scenario = launchFragmentInContainer<ImageDetailsFragment>(factory = fragmentFactory)
        scenario.moveToState(Lifecycle.State.RESUMED)

        // error view is displayed
        Espresso.onView(ViewMatchers.withId(R.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(EXPECTED_ERROR_MESSAGE))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // normal view does not exist
        Espresso.onView(ViewMatchers.withId(R.id.viewPager2))
            .check(ViewAssertions.doesNotExist())
    }
}
