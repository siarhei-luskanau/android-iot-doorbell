package siarhei.luskanau.iot.doorbell.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation

class DefaultAppNavigation(private val activity: Activity) : AppNavigation {

    private val navController: NavController by lazy {
        Navigation.findNavController(activity, R.id.navHostFragment)
    }

    override fun goBack(): Boolean =
        navController.popBackStack()

    override fun goDoorbellListToPermissions() =
        navController.navigate(
            NavRootDirections.actionDoorbellListToPermissions()
        )

    override fun navigateToImageList(doorbellData: DoorbellData) =
        navController.navigate(
            NavRootDirections.actionDoorbellListToImageList(doorbellData)
        )

    override fun navigateToImageDetails(imageData: ImageData) =
        navController.navigate(
            NavRootDirections.actionImageListToImageDetails(imageData)
        )
}
