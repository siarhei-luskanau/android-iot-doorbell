package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraManager;

import java.util.HashMap;
import java.util.Map;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.SchedulerSet;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.camera.CameraRepository;
import siarhei.luskanau.iot.doorbell.camera.ImageCompressor;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.data.firebase.FirebaseImageRepository;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceNameUseCase;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;
import timber.log.Timber;

@Module
public class ApplicationModule {

    private static final String TAG = ApplicationModule.class.getSimpleName();

    private final Application application;

    public ApplicationModule(final Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @ApplicationScope
    DeviceInfo provideDeviceInfo() {
        final Map<String, Object> additionalInfo = new HashMap<>();
        try {
            final CameraManager cameraManager = (CameraManager) this.application.getSystemService(Context.CAMERA_SERVICE);
            additionalInfo.put("CameraIdList", cameraManager.getCameraIdList());
        } catch (final Exception e) {
            Timber.e(e);
        }
        return new DeviceInfo(this.application, additionalInfo);
    }

    @Provides
    @ApplicationScope
    ISchedulerSet provideSchedulerSet() {
        return new SchedulerSet();
    }

    @Provides
    @ApplicationScope
    ImageRepository provideImageRepository() {
        return new FirebaseImageRepository(this.application);
    }

    @Provides
    @ApplicationScope
    ErrorMessageFactory provideErrorMessageFactory() {
        return new SimpleErrorMessageFactory();
    }

    @Provides
    @ApplicationScope
    SendDeviceInfoUseCase provideSendDeviceInfoUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        return new SendDeviceInfoUseCase(imageRepository, schedulerSet);
    }

    @Provides
    @ApplicationScope
    TakeAndSaveImageUseCase provideTakeAndSaveImageUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        final TakePictureRepository takePictureRepository = new CameraRepository(this.application, new ImageCompressor());
        //final TakePictureRepository takePictureRepository = new CameraUsbRepository(this.application);
        return new TakeAndSaveImageUseCase(
                takePictureRepository,
                imageRepository,
                schedulerSet
        );
    }

    @Provides
    @ApplicationScope
    ListenDoorbellUseCase provideListenDoorbellUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        return new ListenDoorbellUseCase(imageRepository, schedulerSet);
    }

    @Provides
    @ApplicationScope
    SendDeviceNameUseCase provideSendDeviceNameUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        return new SendDeviceNameUseCase(imageRepository, schedulerSet);
    }
}
