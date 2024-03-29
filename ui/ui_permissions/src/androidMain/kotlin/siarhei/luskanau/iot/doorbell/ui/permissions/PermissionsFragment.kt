package siarhei.luskanau.iot.doorbell.ui.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import siarhei.luskanau.iot.doorbell.common.AppConstants.PERMISSIONS

class PermissionsFragment(
    presenterProvider: (fragment: Fragment) -> PermissionsPresenter,
) : Fragment() {

    private val presenter: PermissionsPresenter by lazy { presenterProvider(this) }

    private var activityResultLauncher: ActivityResultLauncher<Array<String>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = ComposeView(inflater.context).apply {
        setContent {
            PermissionsComposable()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { result: Map<String, Boolean> ->
            val allGranted = result.values.reduce { acc, b -> acc && b }
            if (allGranted) {
                presenter.onPermissionsGranted()
            } else {
                presenter.onPermissionsNotGranted()
            }
        }

        if (hasPermissions().not()) {
            activityResultLauncher?.launch(PERMISSIONS)
        } else {
            // If permissions have already been granted, proceed
            presenter.onPermissionsGranted()
        }
    }

    private fun hasPermissions(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission,
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}
