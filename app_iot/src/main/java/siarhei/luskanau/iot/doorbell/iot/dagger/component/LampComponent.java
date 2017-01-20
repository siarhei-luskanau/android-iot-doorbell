package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.iot.IotActivity;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.LampModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, LampModule.class})
public interface LampComponent {
    void inject(IotActivity iotActivity);
}
