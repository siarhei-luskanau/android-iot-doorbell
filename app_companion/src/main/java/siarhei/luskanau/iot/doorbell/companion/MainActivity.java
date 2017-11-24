package siarhei.luskanau.iot.doorbell.companion;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.permissions.PermissionCustomer;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.images.ImagesActivity;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImageView;
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellListPresenter;
import siarhei.luskanau.iot.doorbell.ui.doorbells.view.DoorbellListView;
import siarhei.luskanau.iot.doorbell.ui.doorbells.view.IDoorbellListView;
import timber.log.Timber;

public class MainActivity extends BaseComponentActivity implements TakeAndSaveImageView, IDoorbellListView {

    @Inject
    protected DoorbellListPresenter doorbellListPresenter;
    @Inject
    protected TakeAndSaveImagePresenter takeAndSaveImagePresenter;
    @Inject
    protected CameraPermissionsListener cameraPermissionsListener;

    private DoorbellListView doorbellListView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doorbellListView = findViewById(R.id.doorbellListView);
        doorbellListView.setOnDoorbellEntryClickListener(doorbellEntry -> {
            startActivity(ImagesActivity.buildIntent(this, doorbellEntry.getDeviceId()));
        });

        this.initializeInjector();
        takeAndSaveImagePresenter.setView(this);
        doorbellListPresenter.setView(this);

        findViewById(R.id.cameraButton).setOnClickListener(v -> takeAndSaveImage());
        doorbellListPresenter.listenDoorbellList();
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
        doorbellListPresenter.destroy();
    }

    @Override
    public void onDoorbellListUpdated(final List<DoorbellEntry> doorbellEntries) {
        doorbellListView.onDoorbellListUpdated(doorbellEntries);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        doorbellListView.showErrorMessage(errorMessage);
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void takeAndSaveImage() {
        cameraPermissionsListener.checkPermissions(new PermissionCustomer() {

            @Override
            public void onPermissionsGranted() {
                Timber.d("onPermissionsGranted");
                try {
//                    final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
//                    final String[] cameraIdList = cameraManager.getCameraIdList();
//                    for (final String cameraId : cameraIdList) {
//                        takeAndSaveImagePresenter.takeAndSaveImage(cameraId);
//                    }
                    takeAndSaveImagePresenter.takeAndSaveImage(null);
                } catch (final Exception e) {
                    Timber.d(e);
                }
            }

            @Override
            public void onPermissionsDenied() {
                Timber.d("onPermissionsDenied");
            }
        });
    }
}
