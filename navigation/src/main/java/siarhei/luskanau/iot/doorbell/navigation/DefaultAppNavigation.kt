package siarhei.luskanau.iot.doorbell.navigation

import android.app.Activity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

class DefaultAppNavigation(private val activity: Activity) : AppNavigation {

    private fun getNavController(): NavController =
        Navigation.findNavController(activity, R.id.navHostFragment)

    private fun navigateTo(direction: NavDirections) =
        getNavController().navigate(direction)

    override fun goBack(): Boolean =
        getNavController().popBackStack()

    override fun goSplashToDoorbellList() =
        navigateTo(NavRootDirections.actionSplashToDoorbellList())

    override fun goDoorbellListToPermissions() =
        navigateTo(NavRootDirections.actionDoorbellListToPermissions())

    override fun navigateToImageList(doorbellData: DoorbellData) =
        navigateTo(NavRootDirections.actionDoorbellListToImageList(doorbellData))

    override fun navigateToImageDetails(doorbellData: DoorbellData, imageData: ImageData) =
        navigateTo(NavRootDirections.actionImageListToImageDetails(doorbellData, imageData))

    override fun buildImageDetailsArgs(doorbellData: DoorbellData, imageData: ImageData): Bundle =
        NavRootDirections.actionImageListToImageDetails(doorbellData, imageData).arguments
}
