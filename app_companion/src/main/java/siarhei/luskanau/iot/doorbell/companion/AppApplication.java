package siarhei.luskanau.iot.doorbell.companion;

import android.app.Application;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.util.Log;

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
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;

public class AppApplication extends Application {

    private static final String TAG = "AppApplication";
    @Inject
    protected DeviceInfo deviceInfo;
    @Inject
    protected SendDeviceInfoUseCase sendDeviceInfoUseCase;
    @Inject
    protected ListenDoorbellUseCase listenDoorbellUseCase;
    @Inject
    protected TakeAndSaveImageUseCase takeAndSaveImageUseCase;

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
        public void onNext(DoorbellEntry doorbellEntry) {
            if (doorbellEntry.getRing() != null && doorbellEntry.getRing()) {
                try {
                    CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                    String[] cameraIdList = cameraManager.getCameraIdList();
                    if (cameraIdList != null) {
                        for (String cameraId : cameraIdList) {
                            takeAndSaveImageUseCase.execute(new DefaultObserver<>(),
                                    TakeAndSaveImageUseCase.Params.forParams(deviceInfo.getDeviceId(), cameraId));
                        }
                    }
                } catch (CameraAccessException e) {
                    Log.d(TAG, e.getMessage(), e);
                }
            }
        }
    }
}
