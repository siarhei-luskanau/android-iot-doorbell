package siarhei.luskanau.iot.doorbell.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragmentDirections
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentDirections

class DefaultAppNavigation(private val activity: Activity) : AppNavigation {

    private val navController: NavController by lazy {
        Navigation.findNavController(activity, R.id.navHostFragment)
    }

    override fun goBack(): Boolean =
        navController.popBackStack()

    override fun goDoorbellListToPermissions() =
        navController.navigate(
            DoorbellListFragmentDirections.actionDoorbellListToPermissions()
        )

    override fun navigateToImageList(doorbellData: DoorbellData) =
        navController.navigate(
            DoorbellListFragmentDirections.actionDoorbellListToImageList(doorbellData)
        )

    override fun navigateToImageDetails(imageData: ImageData) =
        navController.navigate(
            ImageListFragmentDirections.actionImageListToImageDetails(imageData)
        )
}
