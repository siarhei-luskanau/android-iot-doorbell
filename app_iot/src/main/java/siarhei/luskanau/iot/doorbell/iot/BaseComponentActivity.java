package siarhei.luskanau.iot.doorbell.iot;

import siarhei.luskanau.iot.doorbell.iot.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ActivityModule;

public class BaseComponentActivity extends GrantPermissionsActivity {

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link ActivityModule}
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
