package siarhei.luskanau.iot.doorbell.permissions;

public interface PermissionCustomer {
    void onPermissionsGranted() ;
    void onPermissionsDenied();
}
