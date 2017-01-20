package siarhei.luskanau.android.framework.permissions;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionsGranter {

    private final Activity activity;
    private List<PermissionsListener> permissionsListeners = new ArrayList<>();

    public PermissionsGranter(Activity activity) {
        this.activity = activity;
    }

    public void addPermissionsListener(PermissionsListener permissionsListener) {
        permissionsListeners.add(permissionsListener);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (PermissionsListener permissionsListener : permissionsListeners) {
            permissionsListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean isPermissionsGranted(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
