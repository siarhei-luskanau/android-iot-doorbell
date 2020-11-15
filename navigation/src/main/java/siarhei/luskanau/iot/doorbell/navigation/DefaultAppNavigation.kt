package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import siarhei.luskanau.iot.doorbell.common.AppNavigation
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

    override fun navigateToImageList(doorbellId: String) =
        navigateTo(
            NavRootDirections.actionDoorbellListToImageList(
                doorbellId = doorbellId
            )
        )

    override fun navigateToImageDetails(doorbellId: String, imageId: String) =
        navigateTo(
            NavRootDirections.actionImageListToImageDetails(
                doorbellId = doorbellId,
                imageId = imageId
            )
        )

    override fun buildImageDetailsArgs(doorbellId: String, imageId: String): Bundle =
        NavRootDirections.actionImageListToImageDetails(
            doorbellId = doorbellId,
            imageId = imageId
        ).arguments
}
