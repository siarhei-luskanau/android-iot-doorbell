package siarhei.luskanau.iot.doorbell.companion;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.permissions.PermissionCustomer;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.doorbells.DoorbellEntryAdapter;
import siarhei.luskanau.iot.doorbell.presenter.doorbells.DoorbellListPresenter;
import siarhei.luskanau.iot.doorbell.presenter.doorbells.DoorbellListView;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImageView;

public class MainActivity extends BaseComponentActivity implements TakeAndSaveImageView, DoorbellListView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    protected DoorbellListPresenter doorbellListPresenter;
    @Inject
    protected TakeAndSaveImagePresenter takeAndSaveImagePresenter;
    @Inject
    protected CameraPermissionsListener cameraPermissionsListener;

    private RecyclerView mRecyclerView;
    private DoorbellEntryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        this.initializeInjector();
        takeAndSaveImagePresenter.setView(this);
        doorbellListPresenter.setView(this);
        mAdapter = new DoorbellEntryAdapter();
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.cameraButton).setOnClickListener(v -> {
            cameraPermissionsListener.checkPermissions(new PermissionCustomer() {
                @Override
                public void onPermissionsGranted() {
                    Log.d(TAG, "onPermissionsGranted");
                    takeAndSaveImagePresenter.takeAndSaveImage();
                }

                @Override
                public void onPermissionsDenied() {
                    Log.d(TAG, "onPermissionsDenied");
                }
            });
        });
        doorbellListPresenter.listenDoorbellList();
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
        doorbellListPresenter.destroy();
    }

    @Override
    public void onDoorbellListUpdated(List<DoorbellEntry> doorbellEntries) {
        mAdapter.setData(doorbellEntries);
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
