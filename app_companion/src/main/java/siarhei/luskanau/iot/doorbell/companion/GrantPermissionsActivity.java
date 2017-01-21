package siarhei.luskanau.iot.doorbell.companion;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import siarhei.luskanau.android.framework.permissions.PermissionsGranter;

public abstract class GrantPermissionsActivity extends AppCompatActivity {

    private PermissionsGranter permissionsGranter = new PermissionsGranter(this);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsGranter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public PermissionsGranter getPermissionsGranter() {
        return permissionsGranter;
    }
}
