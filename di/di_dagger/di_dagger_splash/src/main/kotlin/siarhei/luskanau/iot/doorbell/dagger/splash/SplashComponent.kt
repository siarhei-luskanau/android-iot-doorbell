package siarhei.luskanau.iot.doorbell.dagger.splash

import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import siarhei.luskanau.iot.doorbell.ui.splash.SplashNavigation
import javax.inject.Provider

@Component(
    modules = [
        SplashBinderModule::class,
        SplashBuilderModule::class,
    ],
)
interface SplashComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance splashNavigation: SplashNavigation): SplashComponent
    }
}
