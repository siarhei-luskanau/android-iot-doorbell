package siarhei.luskanau.iot.doorbell

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasAndroidxFragmentInjector
import siarhei.luskanau.iot.doorbell.data.UptimeService
import timber.log.Timber
import javax.inject.Inject

class PermissionActivity : AppCompatActivity(), HasAndroidxFragmentInjector {

    @Inject
    lateinit var androidxFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var uptimeService: UptimeService

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 201
    }

    override fun androidxFragmentInjector() = androidxFragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate")

        val permissions = mutableListOf<String>()
        for (permission in AppConstants.PERMISSIONS) {
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
        uptimeService.cameraWorker()
        finish()
    }

    private fun onPermissionsDenied() {
        Timber.d("onPermissionsDenied")
        finish()
    }
}