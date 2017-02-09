package siarhei.luskanau.iot.doorbell.iot.dagger.component;

import android.app.Application;

import dagger.Component;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.iot.AppApplication;
import siarhei.luskanau.iot.doorbell.iot.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.iot.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;
import siarhei.luskanau.iot.doorbell.repository.TakePictureRepository;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(AppApplication appApplication);

    Application application();

    DeviceInfo deviceInfo();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    TakePictureRepository takePictureRepository();

    ImageRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();
}