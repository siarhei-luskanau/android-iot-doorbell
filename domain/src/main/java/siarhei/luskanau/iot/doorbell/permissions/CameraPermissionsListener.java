package siarhei.luskanau.iot.doorbell.permissions;

import android.Manifest;

public class CameraPermissionsListener extends PermissionsListener {

    private static final int PERMISSIONS_REQUEST_CODE = 201;
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
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
