package siarhei.luskanau.iot.doorbell.companion;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerImageComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ImageComponent;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImageView;
import siarhei.luskanau.iot.doorbell.utils.CameraHelper;

public class MainActivity extends BaseComponentActivity implements SendImageView {

    private DatabaseReference mDatabaseRef;

    private RecyclerView mRecyclerView;
    private DoorbellEntryAdapter mAdapter;

    @Inject
    protected SendImagePresenter sendImagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CameraHelper(this).cameraInfo();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("logs");

        this.initializeInjector();
        sendImagePresenter.setView(this);
    }

    private void initializeInjector() {
        ImageComponent imageComponent = DaggerImageComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        imageComponent.inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new DoorbellEntryAdapter(this, mDatabaseRef);
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

        sendImagePresenter.destroy();
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
