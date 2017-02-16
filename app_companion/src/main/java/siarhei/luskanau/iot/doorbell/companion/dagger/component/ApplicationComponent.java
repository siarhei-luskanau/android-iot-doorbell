package siarhei.luskanau.iot.doorbell.companion.dagger.component;

import android.app.Application;

import dagger.Component;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.DeviceInfo;
import siarhei.luskanau.iot.doorbell.companion.AppApplication;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.interactor.TakeAndSaveImageUseCase;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(AppApplication appApplication);

    Application application();

    DeviceInfo deviceInfo();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    TakeAndSaveImageUseCase takeAndSaveImageUseCase();

    ImageRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();
}