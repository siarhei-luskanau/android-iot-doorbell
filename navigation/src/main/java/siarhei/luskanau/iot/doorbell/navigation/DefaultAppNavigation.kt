package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import timber.log.Timber

class DefaultAppNavigation(private val activity: FragmentActivity) : AppNavigation {

    private fun getNavController(): NavController =
        (activity.supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController

    @Suppress("TooGenericExceptionCaught")
    private fun navigateTo(direction: NavDirections) =
        try {
            getNavController().navigate(direction)
        } catch (e: Exception) {
            Timber.e(e)
        }

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
