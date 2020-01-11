package siarhei.luskanau.iot.doorbell.common

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface AppNavigation {
    fun goBack(): Boolean
    fun goDoorbellListToPermissions()
    fun navigateToImageList(doorbellData: DoorbellData)
    fun navigateToImageDetails(doorbellData: DoorbellData, imageData: ImageData)
}
