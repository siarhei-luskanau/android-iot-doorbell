package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.iot.dagger.scope.ActivityScope;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity activity() {
        return this.activity;
    }
}
