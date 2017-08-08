package siarhei.luskanau.iot.doorbell.iot.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.util.Log;

import com.google.android.things.pio.PeripheralManagerService;

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
import siarhei.luskanau.iot.doorbell.data.firebase.FirebaseImageRepository;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceIpAddressUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceNameUseCase;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.IpAddressSource;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

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
            final PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
            additionalInfo.put("GpioList", peripheralManagerService.getGpioList());
            additionalInfo.put("I2cBusList", peripheralManagerService.getI2cBusList());
            additionalInfo.put("PwmList", peripheralManagerService.getPwmList());
            additionalInfo.put("SpiBusList", peripheralManagerService.getSpiBusList());
            additionalInfo.put("UartDeviceList", peripheralManagerService.getUartDeviceList());
            final CameraManager cameraManager = (CameraManager) this.application.getSystemService(Context.CAMERA_SERVICE);
            additionalInfo.put("CameraIdList", cameraManager.getCameraIdList());
        } catch (final Exception e) {
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
    SendDeviceInfoUseCase provideSendDeviceInfoUseCase(final ImageRepository imageRepository,
                                                       final ThreadExecutor threadExecutor,
                                                       final PostExecutionThread postExecutionThread) {
        return new SendDeviceInfoUseCase(imageRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @ApplicationScope
    SendDeviceIpAddressUseCase provideSendDeviceIpAddressUseCase(final ImageRepository imageRepository,
                                                                 final ThreadExecutor threadExecutor,
                                                                 final PostExecutionThread postExecutionThread) {
        return new SendDeviceIpAddressUseCase(imageRepository, new IpAddressSource(), threadExecutor, postExecutionThread);
    }

    @Provides
    @ApplicationScope
    ListenDoorbellUseCase provideListenDoorbellUseCase(final ImageRepository imageRepository,
                                                       final ThreadExecutor threadExecutor,
                                                       final PostExecutionThread postExecutionThread) {
        return new ListenDoorbellUseCase(imageRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @ApplicationScope
    SendDeviceNameUseCase provideSendDeviceNameUseCase(final ImageRepository imageRepository,
                                                       final ThreadExecutor threadExecutor,
                                                       final PostExecutionThread postExecutionThread) {
        return new SendDeviceNameUseCase(imageRepository, threadExecutor, postExecutionThread);
    }
}
