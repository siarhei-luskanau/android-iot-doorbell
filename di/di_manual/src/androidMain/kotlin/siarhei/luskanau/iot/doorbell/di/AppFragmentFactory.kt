package siarhei.luskanau.iot.doorbell.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import timber.log.Timber

@Suppress("LongMethod")
class AppFragmentFactory(
    navHostController: NavHostController,
    private val appModules: AppModules,
) : FragmentFactory() {

    private val appNavigation by lazy { DefaultAppNavigation(navHostController) }

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        Timber.d("AppFragmentFactory:instantiate:$className")
        return when (className) {
            DoorbellListFragment::class.java.name -> {
                DoorbellListFragment { fragment: Fragment ->
                    val viewModelFactory: ViewModelProvider.Factory = AppViewModelFactory(
                        appNavigation = appNavigation,
                        appModules = appModules,
                        args = fragment.arguments,
                    )
                    ViewModelProvider(fragment, viewModelFactory)[DoorbellListViewModel::class.java]
                }
            }

            ImageListFragment::class.java.name -> {
                ImageListFragment { fragment: Fragment ->
                    val viewModelFactory: ViewModelProvider.Factory = AppViewModelFactory(
                        appNavigation = appNavigation,
                        appModules = appModules,
                        args = fragment.arguments,
                    )
                    ViewModelProvider(fragment, viewModelFactory)[ImageListViewModel::class.java]
                }
            }

            ImageDetailsFragment::class.java.name -> {
                ImageDetailsFragment { fragment: Fragment ->
                    val doorbellId = "doorbellId"
                    val imageId = "imageId"
                    ImageDetailsPresenterImpl(
                        doorbellId = doorbellId,
                        imageId = imageId,
                        doorbellRepository = appModules.doorbellRepository,
                    )
                }
            }

            else -> super.instantiate(classLoader, className)
        }
    }
}
