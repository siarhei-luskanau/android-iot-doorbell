package siarhei.luskanau.iot.lamp.remote.control.dagger.component;

import android.app.Activity;

import dagger.Component;
import siarhei.luskanau.iot.lamp.remote.control.dagger.modules.ActivityModule;
import siarhei.luskanau.iot.lamp.remote.control.dagger.scope.ActivityScope;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity activity();
}
