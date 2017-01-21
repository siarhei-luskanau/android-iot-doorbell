package siarhei.luskanau.iot.doorbell.companion;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.permissions.PermissionCustomer;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.camera.TakePictureUseCase;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImageView;

public class MainActivity extends BaseComponentActivity implements SendImageView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    protected SendImagePresenter sendImagePresenter;
    @Inject
    protected CameraPermissionsListener cameraPermissionsListener;
    @Inject
    protected TakePictureUseCase takePictureUseCase;

    private RecyclerView mRecyclerView;
    private DoorbellEntryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));

        this.initializeInjector();
        sendImagePresenter.setView(this);

        findViewById(R.id.cameraButton).setOnClickListener(v -> {
            cameraPermissionsListener.checkPermissions(new PermissionCustomer() {
                @Override
                public void onPermissionsGranted() {
                    Log.d(TAG, "onPermissionsGranted");
                    takePictureUseCase.execute(new TakePictureObserver(), null);
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

        takePictureUseCase.dispose();
        sendImagePresenter.destroy();
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private final class TakePictureObserver extends DefaultObserver<byte[]> {
        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete");
        }

        @Override
        public void onNext(byte[] imageBytes) {
            Log.d(TAG, "onNext: " + imageBytes.length);

            final DatabaseReference log = FirebaseDatabase.getInstance().getReference("logs").push();
            String imageStr = Base64.encodeToString(imageBytes, Base64.NO_WRAP | Base64.URL_SAFE);
            // upload image to firebase
            log.child("timestamp").setValue(ServerValue.TIMESTAMP);
            log.child("image").setValue(imageStr);
            log.child("image_length").setValue(imageBytes.length);
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "onError: " + e);
        }
    }
}
