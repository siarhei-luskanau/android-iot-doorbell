package siarhei.luskanau.iot.doorbell

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import timber.log.Timber

class PermissionActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 201
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        val permissions = mutableListOf<String>()
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(permission)
            }
        }

        if (permissions.isNotEmpty()) {
            Timber.d("requestPermissions")
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSIONS_REQUEST_CODE)
        } else {
            onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("onRequestPermissionsResult")

        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            var granted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }
            if (granted) {
                onPermissionsGranted()
            } else {
                onPermissionsDenied()
            }
        }
    }

    private fun onPermissionsGranted() {
        Timber.d("onPermissionsGranted")
        startService(Intent(this, CameraService::class.java))
        finish()
    }

    private fun onPermissionsDenied() {
        Timber.d("onPermissionsDenied")
        finish()
    }

}