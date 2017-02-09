package siarhei.luskanau.iot.doorbell.companion;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import siarhei.luskanau.android.framework.interactor.DefaultObserver;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.component.DaggerApplicationComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.interactor.SendDeviceInfoUseCase;

public class AppApplication extends Application {

    @Inject
    protected DeviceInfo deviceInfo;
    @Inject
    protected SendDeviceInfoUseCase sendDeviceInfoUseCase;

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeLeakDetection();

        sendDeviceInfoUseCase.execute(new DefaultObserver<>(),
                SendDeviceInfoUseCase.Params.forParams(deviceInfo));
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
}
