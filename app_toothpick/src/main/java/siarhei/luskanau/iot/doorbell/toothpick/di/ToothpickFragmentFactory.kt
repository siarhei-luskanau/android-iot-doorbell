package siarhei.luskanau.iot.doorbell.toothpick.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import timber.log.Timber
import toothpick.Scope

class ToothpickFragmentFactory(
    fragmentActivity: FragmentActivity,
    private val scope: Scope
) : FragmentFactory() {

    private val appNavigation: AppNavigation by lazy { DefaultAppNavigation(fragmentActivity) }
    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        ToothpickViewModelFactory(scope = scope)
    }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Timber.d("AppFragmentFactory:instantiate:$className")
        return when (className) {

            PermissionsFragment::class.java.name -> PermissionsFragment {
                PermissionsPresenter(appNavigation)
            }

            DoorbellListFragment::class.java.name -> {
                DoorbellListFragment { fragment: Fragment ->
                    DoorbellListPresenterImpl(
                        doorbellListViewModel = ViewModelProvider(fragment, viewModelFactory)
                            .get(DoorbellListViewModel::class.java),
                        appNavigation = appNavigation,
                        thisDeviceRepository = scope.getInstance(ThisDeviceRepository::class.java)
                    )
                }
            }

            ImageListFragment::class.java.name -> {
                ImageListFragment { fragment: Fragment ->
                    val doorbellData = fragment.arguments?.let { args: Bundle ->
                        ImageListFragmentArgs.fromBundle(args).doorbellData
                    }
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
                    val doorbellData = fragment.arguments?.let { args ->
                        ImageDetailsFragmentArgs.fromBundle(args).doorbellData
                    }
                    val imageData = fragment.arguments?.let { args ->
                        ImageDetailsFragmentArgs.fromBundle(args).imageData
                    }
                    ImageDetailsPresenterImpl(
                        appNavigation = appNavigation,
                        fragment = fragment,
                        doorbellData = doorbellData,
                        imageData = imageData
                    )
                }
            }

            ImageDetailsSlideFragment::class.java.name -> {
                ImageDetailsSlideFragment { fragment: Fragment ->
                    val imageData = fragment.arguments?.let { args ->
                        ImageDetailsFragmentArgs.fromBundle(args).imageData
                    }
                    ImageDetailsSlidePresenterImpl(
                        imageData = imageData
                    )
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
