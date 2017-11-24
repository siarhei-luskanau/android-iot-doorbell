package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.GrantPermissionsActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellListUseCase;
import siarhei.luskanau.iot.doorbell.interactor.ListenImageListUseCase;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.images.ImagesPresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellListPresenter;

@Module
public class ActivityModule {

    private final GrantPermissionsActivity activity;

    public ActivityModule(final GrantPermissionsActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity activity() {
        return this.activity;
    }

    @Provides
    @ActivityScope
    CameraPermissionsListener provideCameraPermissionsListener() {
        return new CameraPermissionsListener(activity.getPermissionsGranter());
    }

    @Provides
    TakeAndSaveImagePresenter provideTakeAndSaveImagePresenter(
            final TakeAndSaveImageUseCase takeAndSaveImageUseCase,
            final DeviceInfo deviceInfo,
            final ErrorMessageFactory errorMessageFactory) {
        return new TakeAndSaveImagePresenter(
                takeAndSaveImageUseCase,
                deviceInfo,
                errorMessageFactory
        );
    }

    @Provides
    DoorbellListPresenter provideDoorbellListsPresenter(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet,
            final ErrorMessageFactory errorMessageFactory
    ) {
        final ListenDoorbellListUseCase doorbellsUseCase = new ListenDoorbellListUseCase(
                imageRepository,
                schedulerSet
        );
        return new DoorbellListPresenter(doorbellsUseCase, errorMessageFactory);
    }

    @Provides
    ImagesPresenter provideDoorbellPresenter(
            final ImageRepository imageRepository,
            final TakeAndSaveImageUseCase takeAndSaveImageUseCase,
            final DeviceInfo deviceInfo,
            final ISchedulerSet schedulerSet,
            final ErrorMessageFactory errorMessageFactory
    ) {
        final ListenImageListUseCase listenImageListUseCase = new ListenImageListUseCase(
                imageRepository,
                schedulerSet
        );
        return new ImagesPresenter(
                listenImageListUseCase,
                takeAndSaveImageUseCase,
                deviceInfo,
                errorMessageFactory
        );
    }
}
