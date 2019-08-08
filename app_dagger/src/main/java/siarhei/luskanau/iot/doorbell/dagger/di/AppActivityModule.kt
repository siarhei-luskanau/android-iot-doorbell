package siarhei.luskanau.iot.doorbell.dagger.di

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.dagger.AppActivity
import siarhei.luskanau.iot.doorbell.dagger.di.common.PerActivity
import siarhei.luskanau.iot.doorbell.dagger.di.fragment.FragmentBindingModule
import siarhei.luskanau.iot.doorbell.dagger.di.fragment.FragmentBuilderModule
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation

@Module(includes = [
    FragmentBindingModule::class,
    FragmentBuilderModule::class
])
class AppActivityModule {

    @Provides
    @PerActivity
    fun provideAppCompatActivity(activity: AppActivity): FragmentActivity = activity

    @Provides
    @PerActivity
    fun provideAppNavigation(activity: FragmentActivity): AppNavigation = DefaultAppNavigation(activity)
}
