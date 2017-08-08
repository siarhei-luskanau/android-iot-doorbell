package siarhei.luskanau.iot.doorbell.iot;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.DoorbellEntry;
import siarhei.luskanau.iot.doorbell.interactor.ListenDoorbellUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceIpAddressUseCase;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceNameUseCase;
import siarhei.luskanau.iot.doorbell.iot.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.doorbell.iot.dagger.component.DaggerApplicationComponent;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ApplicationModule;

public class AppApplication extends Application {

    private static final String TAG = AppApplication.class.getSimpleName();

    @Inject
    protected DeviceInfo deviceInfo;
    @Inject
    protected SendDeviceInfoUseCase sendDeviceInfoUseCase;
    @Inject
    protected SendDeviceIpAddressUseCase sendDeviceIpAddressUseCase;
    @Inject
    protected ListenDoorbellUseCase listenDoorbellUseCase;
    @Inject
    protected SendDeviceNameUseCase sendDeviceNameUseCase;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Starting AppApplication");
        this.initializeInjector();
        this.initializeLeakDetection();

        sendDeviceInfoUseCase.execute(new DefaultObserver<>(),
                SendDeviceInfoUseCase.Params.forParams(deviceInfo));

        sendDeviceIpAddressUseCase.execute(new DefaultObserver<>(),
                SendDeviceIpAddressUseCase.Params.forParams(deviceInfo.getDeviceId()));

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
        }
    }
}
