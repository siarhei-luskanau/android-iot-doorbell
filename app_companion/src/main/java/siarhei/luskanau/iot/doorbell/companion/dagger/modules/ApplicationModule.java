package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.JobExecutor;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.executor.UIThread;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.camera.CameraRepository;
import siarhei.luskanau.iot.doorbell.camera.ImageCompressor;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.data.firebase.FirebaseImageRepository;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceIpAddressUseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.IpAddressSource;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

@Module
public class ApplicationModule {

    private static final String TAG = ApplicationModule.class.getSimpleName();

    private final Application application;

    public ApplicationModule(Application application) {
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
        Map<String, Object> additionalInfo = new HashMap<>();
        try {
            CameraManager cameraManager = (CameraManager) this.application.getSystemService(Context.CAMERA_SERVICE);
            additionalInfo.put("CameraIdList", cameraManager.getCameraIdList());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return new DeviceInfo(this.application, additionalInfo);
    }

    @Provides
    @ApplicationScope
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    @ApplicationScope
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    @ApplicationScope
    TakePictureRepository provideTakePictureRepository() {
        return new CameraRepository(this.application, new ImageCompressor());
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
    SendDeviceInfoUseCase provideSendDeviceInfoUseCase(ImageRepository imageRepository,
                                                       ThreadExecutor threadExecutor,
                                                       PostExecutionThread postExecutionThread) {
        return new SendDeviceInfoUseCase(imageRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @ApplicationScope
    SendDeviceIpAddressUseCase provideSendDeviceIpAddressUseCase(ImageRepository imageRepository,
                                                                 ThreadExecutor threadExecutor,
                                                                 PostExecutionThread postExecutionThread) {
        return new SendDeviceIpAddressUseCase(imageRepository, new IpAddressSource(), threadExecutor, postExecutionThread);
    }
}
