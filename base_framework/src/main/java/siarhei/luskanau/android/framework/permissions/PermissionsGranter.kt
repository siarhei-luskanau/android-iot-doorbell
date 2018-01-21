package siarhei.luskanau.android.framework.permissions

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat

class PermissionsGranter(private val activity: Activity) {

    private val permissionsListeners = mutableListOf<PermissionsListener>()

    fun addPermissionsListener(permissionsListener: PermissionsListener) {
        permissionsListeners.add(permissionsListener)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        for (permissionsListener in permissionsListeners) {
            permissionsListener.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun isPermissionsGranted(permission: String) =
            ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED


    fun requestPermissions(permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}
