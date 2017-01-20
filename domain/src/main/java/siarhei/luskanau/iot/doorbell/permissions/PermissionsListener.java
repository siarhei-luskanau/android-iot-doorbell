package siarhei.luskanau.iot.doorbell.permissions;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public abstract class PermissionsListener {

    private final PermissionsGranter permissionsGranter;

    private PermissionCustomer permissionCustomer;

    public PermissionsListener(PermissionsGranter permissionsGranter) {
        this.permissionsGranter = permissionsGranter;
        permissionsGranter.addPermissionsListener(this);
    }

    public void checkPermissions(PermissionCustomer permissionCustomer) {
        String[] permissions = getRequiredPermissions();
        for (String permission : permissions) {
            if (!permissionsGranter.isPermissionsGranted(permission)) {
                this.permissionCustomer = permissionCustomer;
                permissionsGranter.requestPermissions(permissions, getRequestCode());
                return;
            }
        }
        permissionCustomer.onPermissionsGranted();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == getRequestCode()) {
            boolean granted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    granted = false;
                    break;
                }
            }
            if (granted) {
                permissionCustomer.onPermissionsGranted();
            } else {
                permissionCustomer.onPermissionsDenied();
            }
        }
        permissionCustomer = null;
    }

    protected abstract String[] getRequiredPermissions();

    protected abstract int getRequestCode();
}
