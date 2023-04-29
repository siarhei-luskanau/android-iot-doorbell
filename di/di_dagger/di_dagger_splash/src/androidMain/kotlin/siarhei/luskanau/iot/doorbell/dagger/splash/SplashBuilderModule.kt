package siarhei.luskanau.iot.doorbell.dagger.splash

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.splash.SplashFragment
import siarhei.luskanau.iot.doorbell.ui.splash.SplashNavigation
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModel
import javax.inject.Provider

@Module
class SplashBuilderModule {

    @Provides
    fun providesFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>,
    ): FragmentFactory = DaggerFragmentFactory(
        providers,
    )

    @Provides
    fun provideSplashViewModel(
        splashNavigation: SplashNavigation,
    ) = SplashViewModel(
        splashNavigation = splashNavigation,
    )

    @Provides
    fun provideSplashFragment(
        viewModelFactory: ViewModelProvider.Factory,
    ) = SplashFragment { fragment: Fragment ->
        ViewModelProvider(fragment, viewModelFactory)
            .get(SplashViewModel::class.java)
    }
}
