package siarhei.luskanau.iot.doorbell.companion.dagger.component;

import android.app.Application;

import dagger.Component;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.camera.CameraRepository;
import siarhei.luskanau.iot.doorbell.companion.dagger.modules.ApplicationModule;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();

    ImageRepository lampRepository();

    ErrorMessageFactory errorMessageFactory();

    CameraRepository cameraRepository();
}