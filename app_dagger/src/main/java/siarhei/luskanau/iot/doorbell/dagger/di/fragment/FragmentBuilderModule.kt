package siarhei.luskanau.iot.doorbell.dagger.di.fragment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation
import siarhei.luskanau.iot.doorbell.doomain.AppNavigationArgs
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

@Module
class FragmentBuilderModule {

    @Provides
    fun providePermissionsFragment(
        appNavigation: AppNavigation
    ) = PermissionsFragment {
        PermissionsPresenter(appNavigation = appNavigation)
    }

    @Provides
    fun provideDoorbellListFragment(
        activity: FragmentActivity,
        viewModelFactory: ViewModelProvider.Factory,
        appNavigation: AppNavigation,
        thisDeviceRepository: ThisDeviceRepository
    ) = DoorbellListFragment {
        val placeListViewModel =
            ViewModelProvider(activity, viewModelFactory).get(DoorbellListViewModel::class.java)
        DoorbellListPresenterImpl(
            doorbellListViewModel = placeListViewModel,
            appNavigation = appNavigation,
            thisDeviceRepository = thisDeviceRepository
        )
    }

    @Provides
    fun provideImageListFragment(
        activity: FragmentActivity,
        viewModelFactory: ViewModelProvider.Factory,
        appNavigation: AppNavigation,
        appNavigationArgs: AppNavigationArgs
    ) = ImageListFragment { args: Bundle? ->
        ImageListPresenterImpl(
            doorbellData = appNavigationArgs.getImagesFragmentArgs(args),
            imageListViewModel = ViewModelProvider(activity, viewModelFactory)
                .get(ImageListViewModel::class.java),
            appNavigation = appNavigation
        )
    }

    @Provides
    fun provideImageDetailsFragment(
        appNavigationArgs: AppNavigationArgs
    ) = ImageDetailsFragment { args: Bundle? ->
        ImageDetailsPresenterImpl(
            imageData = appNavigationArgs.getImageDetailsFragmentArgs(args)
        )
    }
}
