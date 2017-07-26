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
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellListUseCase;
import siarhei.luskanau.iot.doorbell.interactor.ListenImageListUseCase;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import siarhei.luskanau.iot.doorbell.presenter.doorbells.DoorbellListPresenter;
import siarhei.luskanau.iot.doorbell.presenter.images.ImagesPresenter;
import siarhei.luskanau.iot.doorbell.presenter.send.TakeAndSaveImagePresenter;
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
    TakeAndSaveImagePresenter provideTakeAndSaveImagePresenter(TakeAndSaveImageUseCase takeAndSaveImageUseCase,
                                                               DeviceInfo deviceInfo,
                                                               ErrorMessageFactory errorMessageFactory) {
        return new TakeAndSaveImagePresenter(takeAndSaveImageUseCase, deviceInfo, errorMessageFactory);
    }

    @Provides
    DoorbellListPresenter provideDoorbellListsPresenter(ImageRepository imageRepository,
                                                        ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        ErrorMessageFactory errorMessageFactory) {
        ListenDoorbellListUseCase doorbellsUseCase = new ListenDoorbellListUseCase(imageRepository,
                threadExecutor, postExecutionThread);
        return new DoorbellListPresenter(doorbellsUseCase, errorMessageFactory);
    }

    @Provides
    ImagesPresenter provideDoorbellPresenter(ImageRepository imageRepository,
                                             TakeAndSaveImageUseCase takeAndSaveImageUseCase,
                                             DeviceInfo deviceInfo,
                                             ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread,
                                             ErrorMessageFactory errorMessageFactory) {
        ListenImageListUseCase listenImageListUseCase = new ListenImageListUseCase(imageRepository,
                threadExecutor, postExecutionThread);
        return new ImagesPresenter(listenImageListUseCase, takeAndSaveImageUseCase, deviceInfo, errorMessageFactory);
    }
}
