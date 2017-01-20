package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import android.app.Application;

import dagger.Component;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ApplicationScope;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ImageRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();
}