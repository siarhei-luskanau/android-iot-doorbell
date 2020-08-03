package siarhei.luskanau.iot.doorbell.koin.splash.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.ui.splash.SplashFragment
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModel

val splashModule = module {

    factory { (appNavigation: AppNavigation) ->
        SplashFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(appNavigation, fragment.arguments) }
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get(SplashViewModel::class.java)
        }
    }

    factory { (appNavigation: AppNavigation, _: Bundle?) ->
        SplashViewModel(appNavigation = appNavigation)
    }
}
