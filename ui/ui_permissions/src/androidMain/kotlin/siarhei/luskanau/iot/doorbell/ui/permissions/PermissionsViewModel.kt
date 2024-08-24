package siarhei.luskanau.iot.doorbell.ui.permissions

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class PermissionsViewModel : ViewModel() {
    abstract val viewState: StateFlow<PermissionsViewState>
    abstract fun onLaunched()
    abstract fun onRequestPermissionClicked()
    abstract fun onOpenSettingsClicked()
    abstract fun onPermissionScreenCompleted()
}
