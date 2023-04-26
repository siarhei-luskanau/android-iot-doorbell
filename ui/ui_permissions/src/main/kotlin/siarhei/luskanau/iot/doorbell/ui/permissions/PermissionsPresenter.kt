package siarhei.luskanau.iot.doorbell.ui.permissions

import siarhei.luskanau.iot.doorbell.common.AppNavigation

class PermissionsPresenter(
    private val appNavigation: AppNavigation,
) {

    fun onPermissionsGranted() {
        appNavigation.goBack()
    }

    fun onPermissionsNotGranted() {
        appNavigation.goBack()
    }
}
