package siarhei.luskanau.iot.doorbell.companion;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import javax.inject.Inject;

import siarhei.luskanau.iot.doorbell.permissions.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.permissions.PermissionCustomer;
import siarhei.luskanau.iot.doorbell.permissions.PermissionsGranter;

public abstract class GrantPermissionsActivity extends AppCompatActivity {

    private static final String TAG = GrantPermissionsActivity.class.getSimpleName();

    private PermissionsGranter permissionsGranter = new PermissionsGranter(this);

    @Inject
    protected CameraPermissionsListener cameraPermissionsListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsGranter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public PermissionsGranter getPermissionsGranter() {
        return permissionsGranter;
    }

    @Override
    protected void onStart() {
        super.onStart();

        cameraPermissionsListener.checkPermissions(new PermissionCustomer() {
            @Override
            public void onPermissionsGranted() {
                Log.d(TAG, "onPermissionsGranted");
            }

            @Override
            public void onPermissionsDenied() {
                Log.d(TAG, "onPermissionsDenied");
            }
        });
    }
}
