package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.camera.CameraRepository;
import siarhei.luskanau.iot.doorbell.camera.ImageCompressor;
import siarhei.luskanau.iot.doorbell.camera.TakePictureUseCase;
import siarhei.luskanau.iot.doorbell.companion.GrantPermissionsActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;
import siarhei.luskanau.iot.doorbell.interactor.SendImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.send.SendImagePresenter;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

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
    SendImagePresenter provideSendLampStatePresenter(ImageRepository imageRepository,
                                                     ThreadExecutor threadExecutor,
                                                     PostExecutionThread postExecutionThread,
                                                     ErrorMessageFactory errorMessageFactory) {
        SendImageUseCase sendImageUseCase = new SendImageUseCase(imageRepository,
                threadExecutor, postExecutionThread);
        return new SendImagePresenter(sendImageUseCase, errorMessageFactory);
    }

    @Provides
    TakePictureUseCase provideTakePictureUseCase(CameraRepository cameraRepository,
                                                 ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread) {
        ImageCompressor imageCompressor = new ImageCompressor();
        return new TakePictureUseCase(cameraRepository, imageCompressor, threadExecutor, postExecutionThread);
    }
}
