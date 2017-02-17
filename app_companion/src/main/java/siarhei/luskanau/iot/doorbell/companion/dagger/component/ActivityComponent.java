package siarhei.luskanau.iot.doorbell.companion.dagger.component;

import android.app.Activity;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.camera.CameraPermissionsListener;
import siarhei.luskanau.iot.doorbell.companion.images.ImagesActivity;
import siarhei.luskanau.iot.doorbell.companion.MainActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(ImagesActivity imagesActivity);

    Activity activity();

    CameraPermissionsListener cameraPermissionsListener();
}
