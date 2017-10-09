package siarhei.luskanau.android.framework.permissions;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

public abstract class PermissionsListener {

    private final PermissionsGranter permissionsGranter;

    private PermissionCustomer permissionCustomer;

    public PermissionsListener(final PermissionsGranter permissionsGranter) {
        this.permissionsGranter = permissionsGranter;
        permissionsGranter.addPermissionsListener(this);
    }

    public void checkPermissions(final PermissionCustomer permissionCustomer) {
        final String[] permissions = getRequiredPermissions();
        for (final String permission : permissions) {
            if (!permissionsGranter.isPermissionsGranted(permission)) {
                this.permissionCustomer = permissionCustomer;
                permissionsGranter.requestPermissions(permissions, getRequestCode());
                return;
            }
        }
        permissionCustomer.onPermissionsGranted();
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        if (requestCode == getRequestCode()) {
            boolean granted = true;
            for (final int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
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
