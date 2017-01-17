package siarhei.luskanau.iot.lamp.remote.control;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import siarhei.luskanau.iot.lamp.remote.control.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.lamp.remote.control.dagger.component.DaggerApplicationComponent;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.ApplicationModule;

public class AppApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        this.initializeLeakDetection();
    }

    private void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
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
