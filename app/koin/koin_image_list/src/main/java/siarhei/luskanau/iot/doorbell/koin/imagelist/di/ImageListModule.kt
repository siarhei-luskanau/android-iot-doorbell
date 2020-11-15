package siarhei.luskanau.iot.doorbell.koin.imagelist.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.koin.common.di.fragment
import siarhei.luskanau.iot.doorbell.koin.common.di.viewModel
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

val imageListModule = module {

    fragment { appNavigation: AppNavigation ->
        ImageListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                get { parametersOf(appNavigation, fragment.arguments) }
            ViewModelProvider(fragment, viewModelFactory)
                .get(ImageListViewModel::class.java)
        }
    }

    viewModel { appNavigation: AppNavigation, args: Bundle? ->
        val doorbellId = ImageListFragmentArgs.fromBundle(requireNotNull(args)).doorbellId
        ImageListViewModel(
            doorbellId = doorbellId,
            appNavigation = appNavigation,
            doorbellRepository = get(),
            imagesDataSourceFactory = get(),
            uptimeRepository = get()
        )
    }
}
