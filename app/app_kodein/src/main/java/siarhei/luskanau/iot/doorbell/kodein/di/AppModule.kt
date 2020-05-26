package siarhei.luskanau.iot.doorbell.kodein.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.work.WorkManager
import org.kodein.di.Kodein
import org.kodein.di.generic.M
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import siarhei.luskanau.iot.doorbell.cache.DefaultCachedRepository
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.common.DefaultDoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactoryImpl
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.AndroidDeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.AndroidIpAddressProvider
import siarhei.luskanau.iot.doorbell.data.AndroidThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepositoryFake
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.JetpackCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import siarhei.luskanau.iot.doorbell.ui.splash.SplashFragment
import siarhei.luskanau.iot.doorbell.ui.splash.SplashViewModel
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService

val appModule = Kodein.Module(name = "appModule") {
    bind() from singleton { WorkManager.getInstance(instance()) }
    bind<ImageRepository>() with singleton { InternalStorageImageRepository(context = instance()) }
    bind<DoorbellRepository>() with singleton {
        FirebaseDoorbellRepository(imageRepository = instance())
        DoorbellRepositoryFake()
    }
    bind<PersistenceRepository>() with singleton {
        DefaultPersistenceRepository(
            context = instance()
        )
    }
    bind<ScheduleWorkManagerService>() with singleton {
        DefaultScheduleWorkManagerService(workManager = { instance() })
    }
    bind<CachedRepository>() with singleton {
        DefaultCachedRepository(
            doorbellRepository = instance(),
            persistenceRepository = instance()
        )
    }
    bind<CameraRepository>() with singleton {
        JetpackCameraRepository(
            context = instance(),
            imageRepository = instance()
        )
    }
    bind<UptimeRepository>() with singleton { UptimeFirebaseRepository() }
    bind<DoorbellsDataSource>() with singleton {
        DefaultDoorbellsDataSource(
            doorbellRepository = instance()
        )
    }
    bind<DeviceInfoProvider>() with singleton {
        AndroidDeviceInfoProvider(
            context = instance()
        )
    }
    bind<IpAddressProvider>() with singleton { AndroidIpAddressProvider() }
    bind<ImagesDataSourceFactory>() with singleton {
        ImagesDataSourceFactoryImpl(
            cachedRepository = instance()
        )
    }
    bind<ThisDeviceRepository>() with singleton {
        AndroidThisDeviceRepository(
            context = instance(),
            deviceInfoProvider = instance(),
            cameraRepository = instance(),
            ipAddressProvider = instance()
        )
    }
    bind() from singleton {
        AppBackgroundServices(
            doorbellRepository = instance(),
            thisDeviceRepository = instance(),
            scheduleWorkManagerService = instance()
        )
    }
}

val activityModule = Kodein.Module(name = "activityModule") {
    bind<AppNavigation>() with factory { activity: FragmentActivity ->
        DefaultAppNavigation(activity)
    }
    bind<FragmentFactory>() with factory { activity: FragmentActivity ->
        KodeinFragmentFactory(instance(arg = activity), dkodein)
    }
    bind<ViewModelProvider.Factory>() with factory { appNavigation: AppNavigation, args: Bundle? ->
        KodeinViewModelFactory(
            injector = dkodein,
            appNavigation = appNavigation,
            args = args
        )
    }

    // Splash
    bind<Fragment>(
        tag = SplashFragment::class.simpleName
    ) with factory { appNavigation: AppNavigation ->
        SplashFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                instance(arg = M(appNavigation, fragment.arguments))
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get(SplashViewModel::class.java)
        }
    }

    // Permissions
    bind<Fragment>(
        tag = PermissionsFragment::class.simpleName
    ) with factory { appNavigation: AppNavigation ->
        PermissionsFragment {
            instance(arg = appNavigation)
        }
    }
    bind() from factory { appNavigation: AppNavigation ->
        PermissionsPresenter(appNavigation)
    }

    // DoorbellList
    bind<Fragment>(
        tag = DoorbellListFragment::class.simpleName
    ) with factory { appNavigation: AppNavigation ->
        DoorbellListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                instance(arg = M(appNavigation, fragment.arguments))
            ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory)
                .get(DoorbellListViewModel::class.java)
        }
    }

    // ImageList
    bind<Fragment>(
        tag = ImageListFragment::class.simpleName
    ) with factory { appNavigation: AppNavigation ->
        ImageListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                instance(arg = M(appNavigation, fragment.arguments))
            ViewModelProvider(fragment, viewModelFactory)
                .get(ImageListViewModel::class.java)
        }
    }

    // ImageDetails
    bind<Fragment>(
        tag = ImageDetailsFragment::class.simpleName
    ) with factory { appNavigation: AppNavigation ->
        ImageDetailsFragment { fragment: Fragment ->
            val doorbellData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).doorbellData
            }
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            instance(arg = M(appNavigation, fragment, doorbellData, imageData))
        }
    }
    bind() from factory { appNavigation: AppNavigation,
                          fragment: Fragment,
                          doorbellData: DoorbellData,
                          imageData: ImageData ->
        ImageDetailsPresenterImpl(
            appNavigation = appNavigation,
            fragment = fragment,
            doorbellData = doorbellData,
            imageData = imageData
        )
    }

    // ImageDetailsSlide
    bind<Fragment>(
        tag = ImageDetailsSlideFragment::class.simpleName
    ) with factory { _: AppNavigation ->
        ImageDetailsSlideFragment { fragment: Fragment ->
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            instance(arg = imageData)
        }
    }
    bind() from factory { imageData: ImageData ->
        ImageDetailsSlidePresenterImpl(
            imageData = imageData
        )
    }
}

val viewModelModule = Kodein.Module(name = "viewModelModule") {
    bind<ViewModel>(
        tag = SplashViewModel::class.simpleName
    ) with factory { appNavigation: AppNavigation, _: Bundle? ->
        SplashViewModel(appNavigation = appNavigation)
    }

    bind<ViewModel>(
        tag = DoorbellListViewModel::class.simpleName
    ) with factory { appNavigation: AppNavigation, _: Bundle? ->
        DoorbellListViewModel(
            appNavigation = appNavigation,
            doorbellRepository = instance(),
            thisDeviceRepository = instance(),
            cameraRepository = instance(),
            doorbellsDataSource = instance()
        )
    }

    bind<ViewModel>(
        tag = ImageListViewModel::class.simpleName
    ) with factory { appNavigation: AppNavigation, args: Bundle? ->
        val doorbellData = args?.let { ImageListFragmentArgs.fromBundle(it).doorbellData }
        ImageListViewModel(
            doorbellData = doorbellData,
            appNavigation = appNavigation,
            doorbellRepository = instance(),
            imagesDataSourceFactory = instance(),
            uptimeRepository = instance()
        )
    }
}
