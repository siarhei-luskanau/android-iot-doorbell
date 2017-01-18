package siarhei.luskanau.iot.doorbell.companion.dagger.component;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.companion.MainActivity;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ImageModule;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, ImageModule.class})
public interface ImageComponent {
    void inject(MainActivity mainActivity);
}
