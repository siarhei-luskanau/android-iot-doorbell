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
    private final List<PermissionsListener> permissionsListeners = new ArrayList<>();

    public PermissionsGranter(final Activity activity) {
        this.activity = activity;
    }

    public void addPermissionsListener(final PermissionsListener permissionsListener) {
        permissionsListeners.add(permissionsListener);
    }

    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        for (final PermissionsListener permissionsListener : permissionsListeners) {
            permissionsListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean isPermissionsGranted(final String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(final String[] permissions, final int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
