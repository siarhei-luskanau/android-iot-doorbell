package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.GrantPermissionsActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;

@Module
public class ActivityModule {

    private final GrantPermissionsActivity activity;

    public ActivityModule(GrantPermissionsActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    Activity activity() {
        return this.activity;
    }

    @Provides
    @ActivityScope
    CameraPermissionsListener provideCameraPermissionsListener() {
        return new CameraPermissionsListener(activity.getPermissionsGranter());
    }
}
