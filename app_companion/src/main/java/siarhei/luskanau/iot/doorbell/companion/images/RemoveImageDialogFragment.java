package siarhei.luskanau.iot.doorbell.companion.images;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.iot.doorbell.companion.BaseComponentActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ActivityComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerActivityComponent;
import siarhei.luskanau.iot.doorbell.interactor.RemoveImageUseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

public class RemoveImageDialogFragment extends DialogFragment {

    private static final String TAG = RemoveImageDialogFragment.class.getSimpleName();
    private static final String DEVICE_ID = "device_id";
    private static final String NAME_ID = "name_id";
    private static final String IMAGE_ID = "image_id";

    @Inject
    protected ImageRepository imageRepository;
    @Inject
    protected ISchedulerSet schedulerSet;

    private String deviceId;
    private String imageId;

    public static void showFragment(final FragmentActivity activity, final String deviceId, final String name, final String imageId) {
        RemoveImageDialogFragment.newInstance(deviceId, name, imageId).show(activity.getSupportFragmentManager(), TAG);
    }

    private static RemoveImageDialogFragment newInstance(final String deviceId, final String name, final String imageId) {
        final Bundle args = new Bundle();
        args.putString(DEVICE_ID, deviceId);
        args.putString(NAME_ID, name);
        args.putString(IMAGE_ID, imageId);
        final RemoveImageDialogFragment fragment = new RemoveImageDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        deviceId = getArguments().getString(DEVICE_ID);
        final String name = getArguments().getString(NAME_ID);
        imageId = getArguments().getString(IMAGE_ID);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (!TextUtils.isEmpty(name)) {
            builder.setTitle(name);
        } else {
            builder.setTitle(deviceId);
        }
        builder.setMessage("Are you sure to delete image?");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> removeImage());
        builder.setNegativeButton(android.R.string.no, null);

        this.initializeInjector();

        return builder.create();
    }

    private void initializeInjector() {
        final BaseComponentActivity activity = (BaseComponentActivity) getActivity();

        final ActivityComponent activityComponent = DaggerActivityComponent.builder()
                .applicationComponent(activity.getApplicationComponent())
                .activityModule(activity.getActivityModule())
                .build();
        activityComponent.inject(this);
    }

    private void removeImage() {
        final RemoveImageUseCase removeImageUseCase = new RemoveImageUseCase(imageRepository, schedulerSet);
        removeImageUseCase.execute(new DefaultObserver<>(), RemoveImageUseCase.Params.forParams(deviceId, imageId));
    }
}
