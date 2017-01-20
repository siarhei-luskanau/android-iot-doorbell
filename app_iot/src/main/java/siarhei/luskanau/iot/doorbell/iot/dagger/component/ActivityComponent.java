package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import android.app.Activity;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity activity();
}