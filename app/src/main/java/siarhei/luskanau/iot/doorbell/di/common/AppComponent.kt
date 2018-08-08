package siarhei.luskanau.iot.doorbell.di.common

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidxInjectionModule
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.di.AppModule
import siarhei.luskanau.iot.doorbell.work_manager.WorkerModule
import siarhei.luskanau.iot.doorbell.work_manager.dagger.AndroidWorkerInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidxInjectionModule::class,
    ActivityBuildersModule::class,
    AppModule::class,
    AndroidWorkerInjectionModule::class,
    WorkerModule::class
])
interface AppComponent : AndroidInjector<AppApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AppApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(application: AppApplication)
}
