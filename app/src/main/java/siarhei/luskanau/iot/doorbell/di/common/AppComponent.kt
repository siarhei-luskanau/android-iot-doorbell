package siarhei.luskanau.iot.doorbell.di.common

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.di.AppModule
import siarhei.luskanau.iot.doorbell.work_manager.dagger.AndroidWorkerInjectionModule
import siarhei.luskanau.iot.doorbell.work_manager.WorkerModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppActivityModule::class,
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
