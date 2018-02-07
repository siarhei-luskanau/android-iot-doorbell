package siarhei.luskanau.iot.doorbell

import android.support.v4.app.FragmentManager
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellsFragment
import siarhei.luskanau.iot.doorbell.ui.images.ImagesFragment
import javax.inject.Inject

class NavigationController @Inject constructor(appActivity: AppActivity) {

    val containerId: Int = R.id.container
    val fragmentManager: FragmentManager = appActivity.supportFragmentManager

    fun navigateToDoorbells() {
        val fragment = DoorbellsFragment()
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss()
    }

    fun navigateToImages(deviceId: String, deviceName: String? = null) {
        val fragment = ImagesFragment.create(deviceId, deviceName)
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

}