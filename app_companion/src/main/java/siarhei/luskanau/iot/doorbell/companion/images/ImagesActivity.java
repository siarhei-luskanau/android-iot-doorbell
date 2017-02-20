package siarhei.luskanau.iot.doorbell.companion.images;

import android.content.Context;
import android.content.Intent;
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

public class ImagesActivity extends BaseComponentActivity implements ImagesView {

    @Inject
    protected ImagesPresenter imagesPresenter;

    private String deviceId;
    private RecyclerView recyclerView;
    private ImageEntryAdapter adapter;

    public static Intent buildIntent(Context context, String deviceId) {
        return new Intent(context, ImagesActivity.class).putExtra(DomainConstants.DEVICE_ID, deviceId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        deviceId = getIntent().getStringExtra(DomainConstants.DEVICE_ID);
        getSupportActionBar().setTitle(deviceId);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ImageEntryAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((context, holder, position) -> {
            ImageEntry item = adapter.getItem(position);
            RemoveImageDialogFragment.showFragment(ImagesActivity.this, deviceId, null, item.getImageId());
        });

        this.initializeInjector();
        imagesPresenter.setView(this);
        imagesPresenter.listenDoorbell(deviceId);
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

        imagesPresenter.destroy();
    }

    @Override
    public void onImageListUpdated(List<ImageEntry> list) {
        if (list != null) {
            list = Observable.fromIterable(list)
                    .toSortedList((imageEntry1, imageEntry2) ->
                            imageEntry2.getTimestamp().compareTo(imageEntry1.getTimestamp()))
                    .blockingGet();
        }
        adapter.setData(list);
    }

    @Override
    public void showErrorMessage(CharSequence errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
