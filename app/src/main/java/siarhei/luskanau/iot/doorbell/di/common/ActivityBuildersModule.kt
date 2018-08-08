package siarhei.luskanau.iot.doorbell.di.common

import dagger.Module
import dagger.android.ContributesAndroidInjector
import siarhei.luskanau.iot.doorbell.AppActivity
import siarhei.luskanau.iot.doorbell.PermissionActivity
import siarhei.luskanau.iot.doorbell.di.FragmentBuildersModule

@Module
interface ActivityBuildersModule {

    @ContributesAndroidInjector(modules = [
        AppActivityModule::class,
        FragmentBuildersModule::class
    ])
    fun contributeAppActivity(): AppActivity

    @ContributesAndroidInjector
    fun contributePermissionActivity(): PermissionActivity

}