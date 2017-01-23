package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import android.app.Application;

import dagger.Component;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    TakePictureRepository takePictureRepository();

    ImageRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();
}