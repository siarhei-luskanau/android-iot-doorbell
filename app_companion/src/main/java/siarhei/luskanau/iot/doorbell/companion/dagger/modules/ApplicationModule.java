package siarhei.luskanau.iot.doorbell.companion.dagger.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.android.framework.exception.ErrorMessageFactory;
import siarhei.luskanau.android.framework.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.android.framework.executor.JobExecutor;
import siarhei.luskanau.android.framework.executor.PostExecutionThread;
import siarhei.luskanau.android.framework.executor.ThreadExecutor;
import siarhei.luskanau.android.framework.executor.UIThread;
import siarhei.luskanau.iot.doorbell.camera.CameraRepository;
import siarhei.luskanau.iot.doorbell.companion.dagger.scope.ApplicationScope;
import siarhei.luskanau.iot.doorbell.data.firebase.FirebaseImageRepository;
import siarhei.luskanau.iot.doorbell.repository.ImageRepository;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
        return this.application;
    }

    @Provides
    @ApplicationScope
    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    @Provides
    @ApplicationScope
    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    @Provides
    @ApplicationScope
    ImageRepository provideImageRepository() {
        return new FirebaseImageRepository();
    }

    @Provides
    @ApplicationScope
    ErrorMessageFactory provideErrorMessageFactory() {
        return new SimpleErrorMessageFactory();
    }

    @Provides
    @ApplicationScope
    CameraRepository provideCameraRepository() {
        return new CameraRepository(this.application);
    }
}
