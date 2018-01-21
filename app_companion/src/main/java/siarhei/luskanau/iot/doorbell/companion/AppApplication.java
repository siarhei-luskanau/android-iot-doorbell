package siarhei.luskanau.iot.doorbell.companion;

import android.app.Application;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.text.TextUtils;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerApplicationComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceNameUseCase;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import timber.log.Timber;

public class AppApplication extends Application {

    @Inject
    protected DeviceInfo deviceInfo;
    @Inject
    protected SendDeviceInfoUseCase sendDeviceInfoUseCase;
    @Inject
    protected ListenDoorbellUseCase listenDoorbellUseCase;
    @Inject
    protected TakeAndSaveImageUseCase takeAndSaveImageUseCase;
    @Inject
    protected SendDeviceNameUseCase sendDeviceNameUseCase;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeLeakDetection();

        sendDeviceInfoUseCase.execute(new DefaultObserver<>(),
                SendDeviceInfoUseCase.Params.forParams(deviceInfo));

        listenDoorbellUseCase.execute(new ListenDoorbellObserver(),
                ListenDoorbellUseCase.Params.forParams(deviceInfo.getDeviceId()));
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        this.applicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }

    private void initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    private class ListenDoorbellObserver extends DefaultObserver<DoorbellEntry> {

        @Override
        public void onNext(final DoorbellEntry doorbellEntry) {
            if (TextUtils.isEmpty(doorbellEntry.getName())) {
                sendDeviceNameUseCase.execute(new DefaultObserver<>(), SendDeviceNameUseCase.Params.forParams(doorbellEntry.getDeviceId(), Build.MODEL));
            }
            if (doorbellEntry.getRing() != null && doorbellEntry.getRing()) {
                try {
                    final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                    if (cameraManager != null) {
                        final String[] cameraIdList = cameraManager.getCameraIdList();
                        if (cameraIdList.length > 0) {
                            takeAndSaveImageUseCase.execute(new DefaultObserver<>(),
                                    TakeAndSaveImageUseCase.Params.forParams(deviceInfo.getDeviceId(), cameraIdList[0]));
                        }
                    }
                } catch (final CameraAccessException e) {
                    Timber.d(e);
                }
            }
        }
    }
}
