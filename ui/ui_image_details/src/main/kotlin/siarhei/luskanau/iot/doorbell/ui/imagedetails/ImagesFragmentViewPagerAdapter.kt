package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment

class ImagesFragmentViewPagerAdapter(
    private val appNavigation: AppNavigation,
    private val fragment: Fragment,
    private val doorbellId: String,
    private val imageId: String
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment =
        fragment.requireActivity().supportFragmentManager.fragmentFactory.let { fragmentFactory ->
            fragmentFactory.instantiate(
                fragmentFactory.javaClass.classLoader
                    ?: ClassLoader.getSystemClassLoader(),
                ImageDetailsSlideFragment::class.java.name
            ).apply {
                arguments = appNavigation.buildImageDetailsArgs(
                    doorbellId = doorbellId,
                    imageId = imageId
                )
            }
        }
}
