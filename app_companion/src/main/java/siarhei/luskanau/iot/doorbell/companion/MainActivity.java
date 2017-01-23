package siarhei.luskanau.iot.doorbell.companion;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.permissions.PermissionCustomer;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImageView;

public class MainActivity extends BaseComponentActivity implements TakeAndSaveImageView {

    private static final String TAG = MainActivity.class.getSimpleName();

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
    }

    private void initializeInjector() {
        ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        activityComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("logs");
        mAdapter = new DoorbellEntryAdapter(this, databaseReference);
        mRecyclerView.setAdapter(mAdapter);

        // Make sure new events are visible
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        // Tear down Firebase listeners in adapter
        if (mAdapter != null) {
            mAdapter.cleanup();
            mAdapter = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        takeAndSaveImagePresenter.destroy();
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
