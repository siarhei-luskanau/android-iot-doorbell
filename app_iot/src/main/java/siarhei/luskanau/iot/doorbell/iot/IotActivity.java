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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initializeInjector();
        takeAndSaveImagePresenter.setView(this);

        try {
            setContentView(R.layout.activity_main);
            findViewById(R.id.cameraButton).setOnClickListener(v -> {
                cameraPermissionsListener.checkPermissions(new PermissionCustomer() {
                    @Override
                    public void onPermissionsGranted() {
                        Log.d(TAG, "onPermissionsGranted");
                        try {
                            CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                            String[] cameraIdList = cameraManager.getCameraIdList();
                            if (cameraIdList != null) {
                                for (String cameraId : cameraIdList) {
                                    takeAndSaveImagePresenter.takeAndSaveImage(cameraId);
                                }
                            }
                        } catch (CameraAccessException e) {
                            Log.d(TAG, e.getMessage(), e);
                        }
                    }

                    @Override
                    public void onPermissionsDenied() {
                        Log.d(TAG, "onPermissionsDenied");
                    }
                });
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        // Initialize the doorbell button driver
        try {
            button = new Button(GPIO_BUTTON, Button.LogicState.PRESSED_WHEN_LOW);
            button.setOnButtonEventListener(new Button.OnButtonEventListener() {
                @Override
                public void onButtonEvent(Button button, boolean pressed) {
                    if (pressed) {
                        // Doorbell rang!
                        Log.d(TAG, "button pressed");
                        cameraPermissionsListener.checkPermissions(new PermissionCustomer() {
                            @Override
                            public void onPermissionsGranted() {
                                Log.d(TAG, "onPermissionsGranted");
                                try {
                                    CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                                    String[] cameraIdList = cameraManager.getCameraIdList();
                                    if (cameraIdList != null) {
                                        for (String cameraId : cameraIdList) {
                                            takeAndSaveImagePresenter.takeAndSaveImage(cameraId);
                                        }
                                    }
                                } catch (CameraAccessException e) {
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
            });
        } catch (IOException e) {
            Log.e(TAG, "button driver error", e);
        }
    }

    private void initializeInjector() {
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
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
        } catch (IOException e) {
            Log.e(TAG, "button driver error", e);
        }
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
