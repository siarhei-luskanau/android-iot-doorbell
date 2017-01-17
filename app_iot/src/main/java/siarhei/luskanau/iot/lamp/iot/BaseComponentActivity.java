package siarhei.luskanau.iot.lamp.iot;

import android.support.v7.app.AppCompatActivity;

import siarhei.luskanau.iot.lamp.iot.dagger.component.ApplicationComponent;
import siarhei.luskanau.iot.lamp.iot.dagger.modules.ActivityModule;

public class BaseComponentActivity extends AppCompatActivity {

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
