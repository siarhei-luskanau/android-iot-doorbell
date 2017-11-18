package siarhei.luskanau.iot.doorbell.iot.dagger.modules;

import android.app.Activity;
import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import siarhei.luskanau.iot.doorbell.iot.GrantPermissionsActivity;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ActivityScope;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

@Module
public class ActivityModule {

    private final GrantPermissionsActivity activity;

    public ActivityModule(final GrantPermissionsActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Provides
    @ActivityScope
    Activity activity() {
        return this.activity;
    }

    @NonNull
    @Provides
    @ActivityScope
    CameraPermissionsListener provideCameraPermissionsListener() {
        return new CameraPermissionsListener(activity.getPermissionsGranter());
    }

    @NonNull
    @Provides
    TakeAndSaveImagePresenter provideTakeAndSaveImagePresenter(
            @NonNull final TakePictureRepository takePictureRepository,
            @NonNull final ImageRepository imageRepository,
            @NonNull final ISchedulerSet schedulerSet,
            @NonNull final DeviceInfo deviceInfo,
            @NonNull final ErrorMessageFactory errorMessageFactory
    ) {
        final TakeAndSaveImageUseCase takeAndSaveImageUseCase = new TakeAndSaveImageUseCase(
                takePictureRepository,
                imageRepository,
                schedulerSet
        );
        return new TakeAndSaveImagePresenter(
                takeAndSaveImageUseCase,
                deviceInfo,
                errorMessageFactory
        );
    }
}
