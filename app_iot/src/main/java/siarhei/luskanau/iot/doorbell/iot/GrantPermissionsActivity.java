package siarhei.luskanau.iot.doorbell.iot;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import siarhei.luskanau.android.framework.permissions.PermissionsGranter;

public abstract class GrantPermissionsActivity extends AppCompatActivity {

    private final PermissionsGranter permissionsGranter = new PermissionsGranter(this);

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsGranter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public PermissionsGranter getPermissionsGranter() {
        return permissionsGranter;
    }
}
