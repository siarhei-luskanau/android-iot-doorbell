package siarhei.luskanau.iot.doorbell.koin.imagelist.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.koin.common.di.viewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

val imageListModule = module {

    fragment { activity: FragmentActivity ->
        ImageListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(activity, fragment, fragment.arguments) }
            ViewModelProvider(fragment, viewModelFactory)[ImageListViewModel::class.java]
        }
    }

    viewModel { activity: FragmentActivity, _: Fragment, args: Bundle? ->
        val doorbellId = "doorbellId"
        ImageListViewModel(
            doorbellId = doorbellId,
            appNavigation = get { parametersOf(activity) },
            doorbellRepository = get(),
            imagesDataSourceFactory = get()
        )
    }
}
