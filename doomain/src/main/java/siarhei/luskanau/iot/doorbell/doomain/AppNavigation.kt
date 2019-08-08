package siarhei.luskanau.iot.doorbell.doomain

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface AppNavigation {
    fun goBack(): Boolean
    fun goDoorbellListToPermissions()
    fun navigateToImageList(doorbellData: DoorbellData)
    fun navigateToImageDetails(imageData: ImageData)
}
