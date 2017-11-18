package siarhei.luskanau.iot.doorbell.companion.images;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import siarhei.luskanau.iot.doorbell.DomainConstants;
import siarhei.luskanau.iot.doorbell.ImageEntry;
import siarhei.luskanau.iot.doorbell.companion.BaseComponentActivity;
import siarhei.luskanau.iot.doorbell.companion.R;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.presenter.images.ImagesPresenter;
import siarhei.luskanau.iot.doorbell.presenter.images.ImagesView;
import timber.log.Timber;

public class ImagesActivity extends BaseComponentActivity implements ImagesView {

    @Inject
    protected ImagesPresenter imagesPresenter;

    private String deviceId;
    private ImageEntryAdapter adapter;

    public static Intent buildIntent(final Context context, final String deviceId) {
        return new Intent(context, ImagesActivity.class).putExtra(DomainConstants.DEVICE_ID, deviceId);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        deviceId = getIntent().getStringExtra(DomainConstants.DEVICE_ID);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(deviceId);
        }

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ImageEntryAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((context, holder, position) -> {
            final ImageEntry item = adapter.getItem(position);
            RemoveImageDialogFragment.showFragment(this, deviceId, null, item.getImageId());
        });

        this.initializeInjector();
        imagesPresenter.setView(this);
        imagesPresenter.listenDoorbell(deviceId);

        findViewById(R.id.cameraButton).setOnClickListener(v -> {
            try {
                final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                final String[] cameraIdList = cameraManager.getCameraIdList();
                for (final String cameraId : cameraIdList) {
                    imagesPresenter.takeAndSaveImage(cameraId);
                }
            } catch (final CameraAccessException e) {
                Timber.d(e);
            }
        });
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

        imagesPresenter.destroy();
    }

    @Override
    public void onImageListUpdated(List<ImageEntry> list) {
        if (list != null) {
            list = Observable.fromIterable(list)
                    .toSortedList((imageEntry1, imageEntry2) ->
                            Long.compare(imageEntry2.getTimestamp(), imageEntry1.getTimestamp()))
                    .blockingGet();
        }
        adapter.setData(list);
    }

    @Override
    public void showErrorMessage(final CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
