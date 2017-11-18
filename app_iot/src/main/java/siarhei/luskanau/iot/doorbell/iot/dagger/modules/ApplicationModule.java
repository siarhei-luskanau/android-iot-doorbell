package siarhei.luskanau.iot.doorbell.iot.dagger.modules;

import android.app.Application;
import android.content.Context;
import android.hardware.camera2.CameraManager;

import com.google.android.things.pio.PeripheralManagerService;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.android.framework.interactor.ISchedulerSet;
import siarhei.luskanau.android.framework.interactor.SchedulerSet;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.camera.usb.CameraUsbRepository;
import siarhei.luskanau.iot.doorbell.data.firebase.FirebaseImageRepository;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceIpAddressUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceNameUseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.IpAddressSource;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;
import timber.log.Timber;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(final Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @Singleton
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
            Timber.e(e);
        }
        return new DeviceInfo(this.application, additionalInfo);
    }

    @Provides
    @Singleton
    ISchedulerSet provideSchedulerSet() {
        return new SchedulerSet();
    }

    @Provides
    @Singleton
    TakePictureRepository provideTakePictureRepository() {
        //return new CameraRepository(this.application, new ImageCompressor());
        return new CameraUsbRepository(this.application);
    }

    @Provides
    @Singleton
    ImageRepository provideImageRepository() {
        return new FirebaseImageRepository(this.application);
    }

    @Provides
    @Singleton
    ErrorMessageFactory provideErrorMessageFactory() {
        return new SimpleErrorMessageFactory();
    }

    @Provides
    @Singleton
    SendDeviceInfoUseCase provideSendDeviceInfoUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        return new SendDeviceInfoUseCase(
                imageRepository,
                schedulerSet);
    }

    @Provides
    @Singleton
    SendDeviceIpAddressUseCase provideSendDeviceIpAddressUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        return new SendDeviceIpAddressUseCase(
                imageRepository,
                new IpAddressSource(),
                schedulerSet
        );
    }

    @Provides
    @Singleton
    ListenDoorbellUseCase provideListenDoorbellUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet
    ) {
        return new ListenDoorbellUseCase(
                imageRepository,
                schedulerSet
        );
    }

    @Provides
    @Singleton
    SendDeviceNameUseCase provideSendDeviceNameUseCase(
            final ImageRepository imageRepository,
            final ISchedulerSet schedulerSet) {
        return new SendDeviceNameUseCase(
                imageRepository,
                schedulerSet
        );
    }
}
