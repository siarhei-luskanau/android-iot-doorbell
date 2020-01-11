package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import siarhei.luskanau.iot.doorbell.common.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment

class ImagesFragmentViewPagerAdapter(
    private val appNavigationArgs: AppNavigationArgs,
    private val fragment: Fragment,
    private val doorbellData: DoorbellData,
    private val imageData: ImageData
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment =
        fragment.requireActivity().supportFragmentManager.fragmentFactory.let { fragmentFactory ->
            fragmentFactory.instantiate(
                fragmentFactory.javaClass.classLoader
                    ?: ClassLoader.getSystemClassLoader(),
                ImageDetailsSlideFragment::class.java.name
            ).apply {
                arguments = appNavigationArgs.buildImageDetailsArgs(doorbellData, imageData)
            }
        }
}
