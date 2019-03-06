package siarhei.luskanau.iot.doorbell.di.common

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.di.AppModule
import siarhei.luskanau.iot.doorbell.workmanager.dagger.WorkerModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            ActivityBuildersModule::class,
            AppModule::class,
            WorkerModule::class
        ]
)
interface AppComponent : AndroidInjector<AppApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: AppApplication): Builder

        fun build(): AppComponent
    }

    override fun inject(application: AppApplication)
}
