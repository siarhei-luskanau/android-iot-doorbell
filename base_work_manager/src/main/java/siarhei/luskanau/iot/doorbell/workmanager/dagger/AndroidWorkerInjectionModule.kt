package siarhei.luskanau.iot.doorbell.workmanager.dagger

import androidx.work.Worker
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.Multibinds

@Module
abstract class AndroidWorkerInjectionModule {
    @Multibinds
    abstract fun workerInjectorFactories(): Map<Class<out Worker>, AndroidInjector.Factory<out Worker>>
}