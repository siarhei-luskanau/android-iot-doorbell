package siarhei.luskanau.iot.doorbell.ui.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.common.AppConstants.PERMISSIONS

private const val PERMISSIONS_REQUEST_CODE = 201

class PermissionsFragment(
    presenterProvider: (fragment: Fragment) -> PermissionsPresenter
) : Fragment(R.layout.fragment_permissions) {

    private val presenter: PermissionsPresenter by lazy { presenterProvider(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = javaClass.simpleName

        if (hasPermissions().not()) {
            requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        } else {
            // If permissions have already been granted, proceed
            presenter.onPermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.onPermissionsGranted()
            } else {
                presenter.onPermissionsNotGranted()
            }
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}
