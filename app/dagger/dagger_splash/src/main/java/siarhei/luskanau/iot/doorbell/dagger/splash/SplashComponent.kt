package siarhei.luskanau.iot.doorbell.dagger.splash

import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation

@Component(
    modules = [
        SplashBinderModule::class,
        SplashBuilderModule::class
    ]
)
interface SplashComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance appNavigation: AppNavigation): SplashComponent
    }
}
