package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import android.app.Activity;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.iot.IotActivity;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(IotActivity mainActivity);

    Activity activity();

    CameraPermissionsListener cameraPermissionsListener();
}
