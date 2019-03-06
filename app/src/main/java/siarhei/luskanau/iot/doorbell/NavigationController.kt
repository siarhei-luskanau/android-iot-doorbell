package siarhei.luskanau.iot.doorbell

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellsFragmentDirections
import siarhei.luskanau.iot.doorbell.ui.images.ImagesFragmentDirections
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: AppCompatActivity) {

    fun navigateToImages(doorbellId: String, deviceName: String) {
        val direction = DoorbellsFragmentDirections.actionDoorbellsToImages(doorbellId, deviceName)
        activity.findNavController(R.id.navHostFragment).navigate(direction)
    }

    fun navigateToImageDetails(doorbellId: String, imageId: String, deviceName: String) {
        val direction = ImagesFragmentDirections.actionImagesToImageDetails(doorbellId, imageId, deviceName)
        activity.findNavController(R.id.navHostFragment).navigate(direction)
    }
}