package siarhei.luskanau.iot.lamp.remote.control.dagger.component;

import dagger.Component;
import siarhei.luskanau.iot.lamp.remote.control.MainActivity;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.LampModule;
import siarhei.luskanau.iot.lamp.remote.control.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LampModule.class})
public interface LampComponent {
    void inject(MainActivity mainActivity);
}
