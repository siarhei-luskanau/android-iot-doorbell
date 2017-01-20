package siarhei.luskanau.iot.doorbell.iot;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import siarhei.luskanau.iot.doorbell.iot.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.doorbell.iot.dagger.component.DaggerApplicationComponent;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ApplicationModule;

public class AppApplication extends Application {

    private static final String TAG = AppApplication.class.getSimpleName();
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Starting AppApplication");
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
