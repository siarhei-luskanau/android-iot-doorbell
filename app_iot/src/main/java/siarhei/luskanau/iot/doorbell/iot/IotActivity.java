package siarhei.luskanau.iot.doorbell.iot;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.things.contrib.driver.button.Button;

import java.io.IOException;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.permissions.PermissionCustomer;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.iot.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.iot.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImageView;

public class IotActivity extends BaseComponentActivity implements TakeAndSaveImageView {

    private static final String TAG = IotActivity.class.getSimpleName();
    private static final String GPIO_BUTTON = "BCM22";

    @Inject
    protected TakeAndSaveImagePresenter takeAndSaveImagePresenter;
    @Inject
    protected CameraPermissionsListener cameraPermissionsListener;

    private Button button;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initializeInjector();
        takeAndSaveImagePresenter.setView(this);

        try {
            setContentView(R.layout.activity_main);
            findViewById(R.id.cameraButton).setOnClickListener(v -> takeAndSaveImage());
        } catch (final Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        // Initialize the doorbell button driver
        try {
            button = new Button(GPIO_BUTTON, Button.LogicState.PRESSED_WHEN_LOW);
            button.setOnButtonEventListener((button1, pressed) -> {
                if (pressed) {
                    // Doorbell rang!
                    Log.d(TAG, "button pressed");
                    takeAndSaveImage();
                }
            });
        } catch (final IOException e) {
            Log.e(TAG, "button driver error", e);
        }
    }

    private void initializeInjector() {
        final ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        activityComponent.inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        takeAndSaveImagePresenter.destroy();

        try {
            button.close();
        } catch (final IOException e) {
            Log.e(TAG, "button driver error", e);
        }
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void takeAndSaveImage() {
        cameraPermissionsListener.checkPermissions(new PermissionCustomer() {

            @Override
            public void onPermissionsGranted() {
                Log.d(TAG, "onPermissionsGranted");
                try {
                    final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                    final String[] cameraIdList = cameraManager.getCameraIdList();
                    for (final String cameraId : cameraIdList) {
                        takeAndSaveImagePresenter.takeAndSaveImage(cameraId);
                    }
                    takeAndSaveImagePresenter.takeAndSaveImage(null);
                } catch (final CameraAccessException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
            }

            @Override
            public void onPermissionsDenied() {
                Log.d(TAG, "onPermissionsDenied");
            }
        });
    }
}
