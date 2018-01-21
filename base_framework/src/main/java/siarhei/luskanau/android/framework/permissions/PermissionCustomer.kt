package siarhei.luskanau.android.framework.permissions

interface PermissionCustomer {
    fun onPermissionsGranted()
    fun onPermissionsDenied()
}
