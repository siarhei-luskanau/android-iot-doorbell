package siarhei.luskanau.iot.doorbell.di.common

import dagger.Module
import dagger.android.ContributesAndroidInjector
import siarhei.luskanau.iot.doorbell.AppActivity
import siarhei.luskanau.iot.doorbell.di.FragmentBuildersModule

@Module
abstract class AppActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    internal abstract fun contributeAppActivity(): AppActivity

}