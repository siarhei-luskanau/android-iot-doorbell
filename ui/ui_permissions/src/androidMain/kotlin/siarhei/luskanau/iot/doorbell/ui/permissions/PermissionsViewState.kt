package siarhei.luskanau.iot.doorbell.ui.permissions

sealed interface PermissionsViewState {
    data object DeniedPermissionsViewState : PermissionsViewState
    data object GrantedPermissionsViewState : PermissionsViewState
    data object NotGrantedPermissionsViewState : PermissionsViewState
}
