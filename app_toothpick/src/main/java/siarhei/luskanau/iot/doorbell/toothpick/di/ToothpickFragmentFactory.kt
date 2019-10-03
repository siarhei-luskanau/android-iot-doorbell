package siarhei.luskanau.iot.doorbell.toothpick.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation
import siarhei.luskanau.iot.doorbell.doomain.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import timber.log.Timber
import toothpick.Scope

class ToothpickFragmentFactory(
    private val activity: FragmentActivity,
    private val scope: Scope
) : FragmentFactory() {

    private val appNavigation: AppNavigation by lazy { DefaultAppNavigation(activity) }
    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        ToothpickViewModelFactory(scope = scope)
    }

    private val appNavigationArgs: AppNavigationArgs by lazy {
        scope.getInstance(AppNavigationArgs::class.java)
    }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Timber.d("AppFragmentFactory:instantiate:$className")
        return when (className) {

            PermissionsFragment::class.java.name -> PermissionsFragment {
                PermissionsPresenter(appNavigation)
            }

            DoorbellListFragment::class.java.name -> {
                val viewModel = ViewModelProvider(activity, viewModelFactory)
                    .get(DoorbellListViewModel::class.java)
                DoorbellListFragment {
                    DoorbellListPresenterImpl(
                        doorbellListViewModel = viewModel,
                        appNavigation = appNavigation,
                        thisDeviceRepository = scope.getInstance(ThisDeviceRepository::class.java)
                    )
                }
            }

            ImageListFragment::class.java.name -> {
                val viewModel = ViewModelProvider(activity, viewModelFactory)
                    .get(ImageListViewModel::class.java)
                ImageListFragment { args: Bundle? ->
                    val doorbellData = appNavigationArgs.getImagesFragmentArgs(args)
                    ImageListPresenterImpl(
                        doorbellData = requireNotNull(doorbellData),
                        imageListViewModel = viewModel,
                        appNavigation = appNavigation
                    )
                }
            }

            ImageDetailsFragment::class.java.name -> {
                ImageDetailsFragment { args: Bundle? ->
                    val imageData = appNavigationArgs.getImageDetailsFragmentArgs(args)
                    ImageDetailsPresenterImpl(imageData = imageData)
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
