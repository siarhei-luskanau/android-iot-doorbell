package siarhei.luskanau.iot.doorbell.toothpick.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import siarhei.luskanau.iot.doorbell.ui.splash.SplashFragment
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModel
import timber.log.Timber
import toothpick.Scope

@Suppress("LongMethod")
class ToothpickFragmentFactory(
    fragmentActivity: FragmentActivity,
    private val scope: Scope
) : FragmentFactory() {

    private val appNavigation by lazy { DefaultAppNavigation(fragmentActivity) }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Timber.d("AppFragmentFactory:instantiate:$className")
        return when (className) {

            SplashFragment::class.java.name -> {
                SplashFragment { fragment: Fragment ->
                    val viewModelFactory: ViewModelProvider.Factory = ToothpickViewModelFactory(
                        scope = scope,
                        appNavigation = appNavigation,
                        args = fragment.arguments
                    )
                    ViewModelProvider(fragment, viewModelFactory)
                        .get(SplashViewModel::class.java)
                }
            }

            PermissionsFragment::class.java.name -> PermissionsFragment {
                PermissionsPresenter(appNavigation)
            }

            DoorbellListFragment::class.java.name -> {
                DoorbellListFragment { fragment: Fragment ->
                    val viewModelFactory: ViewModelProvider.Factory = ToothpickViewModelFactory(
                        scope = scope,
                        appNavigation = appNavigation,
                        args = fragment.arguments
                    )
                    ViewModelProvider(fragment, viewModelFactory)
                        .get(DoorbellListViewModel::class.java)
                }
            }

            ImageListFragment::class.java.name -> {
                ImageListFragment { fragment: Fragment ->
                    val viewModelFactory: ViewModelProvider.Factory = ToothpickViewModelFactory(
                        scope = scope,
                        appNavigation = appNavigation,
                        args = fragment.arguments
                    )
                    ViewModelProvider(fragment, viewModelFactory)
                        .get(ImageListViewModel::class.java)
                }
            }

            ImageDetailsFragment::class.java.name -> {
                ImageDetailsFragment { fragment: Fragment ->
                    val doorbellId = ImageDetailsFragmentArgs.fromBundle(
                        requireNotNull(fragment.arguments)
                    ).doorbellId
                    val imageId = ImageDetailsFragmentArgs.fromBundle(
                        requireNotNull(fragment.arguments)
                    ).imageId
                    ImageDetailsPresenterImpl(
                        appNavigation = appNavigation,
                        fragment = fragment,
                        doorbellId = doorbellId,
                        imageId = imageId,
                    )
                }
            }

            ImageDetailsSlideFragment::class.java.name -> {
                ImageDetailsSlideFragment { fragment: Fragment ->
                    val doorbellId = ImageDetailsFragmentArgs.fromBundle(
                        requireNotNull(fragment.arguments)
                    ).doorbellId
                    val imageId = ImageDetailsFragmentArgs.fromBundle(
                        requireNotNull(fragment.arguments)
                    ).imageId
                    ImageDetailsSlidePresenterImpl(
                        doorbellId = doorbellId,
                        imageId = imageId,
                        doorbellRepository = scope.getInstance(DoorbellRepository::class.java)
                    )
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
