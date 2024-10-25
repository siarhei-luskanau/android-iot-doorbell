package siarhei.luskanau.iot.doorbell.navigation

import android.os.Bundle
import androidx.navigation.NavHostController
import siarhei.luskanau.iot.doorbell.common.AppNavigation

class DefaultAppNavigation(
    private val navHostController: NavHostController,
) : AppNavigation {

    override fun goBack(): Boolean = navHostController.popBackStack()

    override fun goDoorbellListToPermissions() = Unit

    override fun navigateToImageList(doorbellId: String) = Unit

    override fun navigateToImageDetails(doorbellId: String, imageId: String) = Unit

    override fun buildImageDetailsArgs(doorbellId: String, imageId: String): Bundle = Bundle()
}
