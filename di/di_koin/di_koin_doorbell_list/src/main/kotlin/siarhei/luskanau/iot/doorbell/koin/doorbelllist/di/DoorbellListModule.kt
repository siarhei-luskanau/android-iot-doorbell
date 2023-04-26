package siarhei.luskanau.iot.doorbell.koin.doorbelllist.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.koin.common.di.viewModel
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel

val doorbellListModule = module {

    fragment { activity: FragmentActivity ->
        DoorbellListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(activity, fragment, fragment.arguments) }
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get(DoorbellListViewModel::class.java)
        }
    }

    viewModel { activity: FragmentActivity, _: Fragment, _: Bundle? ->
        DoorbellListViewModel(
            appNavigation = get { parametersOf(activity) },
            thisDeviceRepository = get(),
            doorbellsDataSource = get(),
        )
    }
}
