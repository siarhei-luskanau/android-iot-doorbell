package siarhei.luskanau.iot.doorbell.navigation

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

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

    override fun navigateToImageDetails(doorbellData: DoorbellData, imageData: ImageData) =
        navController.navigate(
            NavRootDirections.actionImageListToImageDetails(doorbellData, imageData)
        )

    override fun buildImageDetailsArgs(doorbellData: DoorbellData, imageData: ImageData): Bundle =
        NavRootDirections.actionImageListToImageDetails(doorbellData, imageData).arguments
}
