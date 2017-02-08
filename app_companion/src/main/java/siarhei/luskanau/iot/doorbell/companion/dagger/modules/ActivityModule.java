package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.GrantPermissionsActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

@Module
public class ActivityModule {

    private final GrantPermissionsActivity activity;

    public ActivityModule(GrantPermissionsActivity activity) {
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
    TakeAndSaveImagePresenter provideTakeAndSaveImagePresenter(TakePictureRepository takePictureRepository,
                                                               ImageRepository imageRepository,
                                                               ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               DeviceInfo deviceInfo,
                                                               ErrorMessageFactory errorMessageFactory) {
        TakeAndSaveImageUseCase takeAndSaveImageUseCase = new TakeAndSaveImageUseCase(takePictureRepository,
                imageRepository, threadExecutor, postExecutionThread);
        return new TakeAndSaveImagePresenter(takeAndSaveImageUseCase, deviceInfo, errorMessageFactory);
    }
}
