package siarhei.luskanau.iot.doorbell.companion;

import siarhei.luskanau.iot.doorbell.companion.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ActivityModule;

public class BaseComponentActivity extends GrantPermissionsActivity {

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    public ApplicationComponent getApplicationComponent() {
        return ((AppApplication) getApplication()).getApplicationComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link ActivityModule}
     */
    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
