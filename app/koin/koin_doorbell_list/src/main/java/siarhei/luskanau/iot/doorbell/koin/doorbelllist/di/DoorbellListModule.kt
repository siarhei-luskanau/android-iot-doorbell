package siarhei.luskanau.iot.doorbell.koin.doorbelllist.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.koin.common.di.viewModel
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel

val doorbellListModule = module {

    fragment { appNavigation: AppNavigation ->
        DoorbellListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(appNavigation, fragment.arguments) }
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get(DoorbellListViewModel::class.java)
        }
    }

    viewModel { appNavigation: AppNavigation, _: Bundle? ->
        DoorbellListViewModel(
            appNavigation = appNavigation,
            thisDeviceRepository = get(),
            doorbellsDataSource = get()
        )
    }
}
