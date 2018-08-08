package siarhei.luskanau.iot.doorbell

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellsFragment
import siarhei.luskanau.iot.doorbell.ui.images.ImagesFragment
import javax.inject.Inject

class NavigationController @Inject constructor(activity: AppCompatActivity) {

    val containerId: Int = R.id.container
    val fragmentManager: FragmentManager = activity.supportFragmentManager

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