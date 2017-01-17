package siarhei.luskanau.iot.lamp.iot.dagger.modules;

import android.app.Application;

import dagger.Module;
import dagger.Provides;
import siarhei.luskanau.iot.lamp.data.firebase.FirebaseImageRepository;
import siarhei.luskanau.iot.doorbell.domain.ImageRepository;
import siarhei.luskanau.iot.doorbell.domain.exception.ErrorMessageFactory;
import siarhei.luskanau.iot.doorbell.domain.exception.SimpleErrorMessageFactory;
import siarhei.luskanau.iot.doorbell.domain.executor.JobExecutor;
import siarhei.luskanau.iot.doorbell.domain.executor.PostExecutionThread;
import siarhei.luskanau.iot.doorbell.domain.executor.ThreadExecutor;
import siarhei.luskanau.iot.doorbell.domain.executor.UIThread;
import siarhei.luskanau.iot.lamp.iot.dagger.scope.ApplicationScope;

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
    ImageRepository provideLampRepository() {
        return new FirebaseImageRepository();
    }

    @Provides
    @ApplicationScope
    ErrorMessageFactory provide() {
        return new SimpleErrorMessageFactory();
    }
}
