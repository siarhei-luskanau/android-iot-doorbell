package siarhei.luskanau.iot.doorbell.koin.di

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation

val appModule = module {

    factory<AppNavigation> { (activity: FragmentActivity) -> DefaultAppNavigation(activity) }

    factory<FragmentFactory> { (activity: FragmentActivity) ->
        val appNavigation: AppNavigation = get { parametersOf(activity) }
        KoinFragmentFactory(
            koin = getKoin(),
            appNavigation = appNavigation
        )
    }

    factory<ViewModelProvider.Factory> { (appNavigation: AppNavigation, args: Bundle?) ->
        KoinViewModelFactory(
            koin = getKoin(),
            appNavigation = appNavigation,
            args = args
        )
    }
}
