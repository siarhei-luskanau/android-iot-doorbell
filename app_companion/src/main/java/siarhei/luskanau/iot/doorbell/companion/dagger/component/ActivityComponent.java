package siarhei.luskanau.iot.doorbell.companion.dagger.component;

import android.app.Activity;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity activity();

    CameraPermissionsListener cameraPermissionsListener();
}
