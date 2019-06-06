package siarhei.luskanau.iot.doorbell.ui

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation

class StubAppNavigation : AppNavigation {
    override fun goBack(): Boolean = true
    override fun goDoorbellListToPermissions() {}
    override fun navigateToImageList(doorbellData: DoorbellData) {}
    override fun navigateToImageDetails(imageData: ImageData) {}
}