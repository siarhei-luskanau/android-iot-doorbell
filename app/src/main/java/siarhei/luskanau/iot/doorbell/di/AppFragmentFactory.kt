package siarhei.luskanau.iot.doorbell.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import siarhei.luskanau.iot.doorbell.common.AppNavigation
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

class AppFragmentFactory(
    fragmentActivity: FragmentActivity,
    private val appModules: AppModules
) : FragmentFactory() {

    private val appNavigation: AppNavigation by lazy { DefaultAppNavigation(fragmentActivity) }

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        AppViewModelFactory(appModules = appModules)
    }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Timber.d("AppFragmentFactory:instantiate:$className")
        return when (className) {

            PermissionsFragment::class.java.name -> {
                PermissionsFragment { _: Bundle?, _: LifecycleOwner ->
                    PermissionsPresenter(appNavigation)
                }
            }

            DoorbellListFragment::class.java.name -> {
                DoorbellListFragment { _: Bundle?, lifecycleOwner: LifecycleOwner ->
                    DoorbellListPresenterImpl(
                        doorbellListViewModel = ViewModelProvider(
                            lifecycleOwner as ViewModelStoreOwner,
                            viewModelFactory
                        ).get(DoorbellListViewModel::class.java),
                        appNavigation = appNavigation,
                        thisDeviceRepository = appModules.thisDeviceRepository
                    )
                }
            }

            ImageListFragment::class.java.name -> {
                ImageListFragment { args: Bundle?, lifecycleOwner: LifecycleOwner ->
                    val doorbellData = appModules.appNavigationArgs.getImagesFragmentArgs(args)
                    ImageListPresenterImpl(
                        doorbellData = requireNotNull(doorbellData),
                        imageListViewModel = ViewModelProvider(
                            lifecycleOwner as ViewModelStoreOwner,
                            viewModelFactory
                        ).get(ImageListViewModel::class.java),
                        appNavigation = appNavigation
                    )
                }
            }

            ImageDetailsFragment::class.java.name -> {
                ImageDetailsFragment { args: Bundle?, _: LifecycleOwner ->
                    val imageData = appModules.appNavigationArgs.getImageDetailsFragmentArgs(args)
                    ImageDetailsPresenterImpl(imageData = imageData)
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
