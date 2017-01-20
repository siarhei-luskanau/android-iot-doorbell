package siarhei.luskanau.iot.doorbell.camera;

import android.Manifest;

import siarhei.luskanau.android.framework.permissions.PermissionsGranter;
import siarhei.luskanau.android.framework.permissions.PermissionsListener;

public class CameraPermissionsListener extends PermissionsListener {

    private static final int PERMISSIONS_REQUEST_CODE = 201;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
    };

    public CameraPermissionsListener(PermissionsGranter permissionsGranter) {
        super(permissionsGranter);
    }

    @Override
    protected int getRequestCode() {
        return PERMISSIONS_REQUEST_CODE;
    }

    @Override
    protected String[] getRequiredPermissions() {
        return PERMISSIONS;
    }
}
