package siarhei.luskanau.iot.doorbell.common

import android.os.Bundle

interface AppNavigation {
    fun goBack(): Boolean
    fun goSplashToDoorbellList()
    fun goDoorbellListToPermissions()
    fun navigateToImageList(doorbellId: String)
    fun navigateToImageDetails(doorbellId: String, imageId: String)
    fun buildImageDetailsArgs(doorbellId: String, imageId: String): Bundle
}
