package siarhei.luskanau.iot.lamp.iot.dagger.component;

import dagger.Component;
import siarhei.luskanau.iot.lamp.iot.BlinkActivity;
import siarhei.luskanau.iot.lamp.iot.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.lamp.iot.dagger.modules.LampModule;
import siarhei.luskanau.iot.lamp.iot.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LampModule.class})
public interface LampComponent {
    void inject(BlinkActivity blinkActivity);
}
