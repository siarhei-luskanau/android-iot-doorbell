package siarhei.luskanau.iot.doorbell.koin.splash.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.koin.common.di.viewModel
import siarhei.luskanau.iot.doorbell.ui.splash.SplashFragment
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModel

val splashModule = module {

    fragment { activity: FragmentActivity ->
        SplashFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(activity, fragment, fragment.arguments) }
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get(SplashViewModel::class.java)
        }
    }

    viewModel { activity: FragmentActivity, _: Fragment, _: Bundle? ->
        SplashViewModel(splashNavigation = get { parametersOf(activity) })
    }
}
