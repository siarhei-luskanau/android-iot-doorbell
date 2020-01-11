package siarhei.luskanau.iot.doorbell.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl
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
                PermissionsFragment { fragment: Fragment ->
                    PermissionsPresenter(appNavigation)
                }
            }

            DoorbellListFragment::class.java.name -> {
                DoorbellListFragment { fragment: Fragment ->
                    DoorbellListPresenterImpl(
                        doorbellListViewModel = ViewModelProvider(fragment, viewModelFactory)
                            .get(DoorbellListViewModel::class.java),
                        appNavigation = appNavigation,
                        thisDeviceRepository = appModules.thisDeviceRepository
                    )
                }
            }

            ImageListFragment::class.java.name -> {
                ImageListFragment { fragment: Fragment ->
                    val doorbellData =
                        appModules.appNavigationArgs.getImagesFragmentArgs(fragment.arguments)
                    ImageListPresenterImpl(
                        doorbellData = requireNotNull(doorbellData),
                        imageListViewModel = ViewModelProvider(fragment, viewModelFactory)
                            .get(ImageListViewModel::class.java),
                        appNavigation = appNavigation
                    )
                }
            }

            ImageDetailsFragment::class.java.name -> {
                ImageDetailsFragment { fragment: Fragment ->
                    val doorbellData = appModules.appNavigationArgs
                        .getDoorbellDataImageDetailsFragmentArgs(fragment.arguments)
                    val imageData = appModules.appNavigationArgs
                        .getImageDataImageDetailsFragmentArgs(fragment.arguments)
                    ImageDetailsPresenterImpl(
                        appNavigationArgs = appModules.appNavigationArgs,
                        fragment = fragment,
                        doorbellData = doorbellData,
                        imageData = imageData
                    )
                }
            }

            ImageDetailsSlideFragment::class.java.name -> {
                ImageDetailsSlideFragment { fragment: Fragment ->
                    val imageData = appModules.appNavigationArgs
                        .getImageDataImageDetailsFragmentArgs(fragment.arguments)
                    ImageDetailsSlidePresenterImpl(
                        imageData = imageData
                    )
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
