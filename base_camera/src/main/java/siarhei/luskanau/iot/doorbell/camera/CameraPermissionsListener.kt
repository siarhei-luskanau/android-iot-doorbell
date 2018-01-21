package siarhei.luskanau.iot.doorbell.camera

import android.Manifest

import siarhei.luskanau.android.framework.permissions.PermissionsGranter
import siarhei.luskanau.android.framework.permissions.PermissionsListener

class CameraPermissionsListener(permissionsGranter: PermissionsGranter) : PermissionsListener(permissionsGranter) {

    override fun getRequestCode(): Int {
        return PERMISSIONS_REQUEST_CODE
    }

    override fun getRequiredPermissions(): Array<String> {
        return PERMISSIONS
    }

    companion object {

        private val PERMISSIONS_REQUEST_CODE = 201
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
