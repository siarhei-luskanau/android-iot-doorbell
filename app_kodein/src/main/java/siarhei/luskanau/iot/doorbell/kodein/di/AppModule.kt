package siarhei.luskanau.iot.doorbell.kodein.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import org.kodein.di.Kodein
import org.kodein.di.generic.M
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import siarhei.luskanau.iot.doorbell.cache.DefaultCachedRepository
import siarhei.luskanau.iot.doorbell.data.AndroidDeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.AndroidIpAddressProvider
import siarhei.luskanau.iot.doorbell.data.AndroidThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.DefaultSchedulerSet
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.JetpackCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.doomain.AppNavigation
import siarhei.luskanau.iot.doorbell.doomain.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.doomain.DefaultDoorbellsDataSource
import siarhei.luskanau.iot.doorbell.doomain.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.doomain.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactoryImpl
import siarhei.luskanau.iot.doorbell.doomain.IpAddressProvider
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigationArgs
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenter
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenter
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import timber.log.Timber

val appModule = Kodein.Module(name = "appModule") {
    bind<SchedulerSet>() with singleton { DefaultSchedulerSet() }
    bind() from singleton { WorkManager.getInstance(instance()) }
    bind<ViewModelProvider.Factory>() with singleton { KodeinViewModelFactory(injector = dkodein) }
    bind<AppNavigationArgs>() with singleton { DefaultAppNavigationArgs() }
    bind<ImageRepository>() with singleton { InternalStorageImageRepository(context = instance()) }
    bind<DoorbellRepository>() with singleton { FirebaseDoorbellRepository(imageRepository = instance()) }
    bind<PersistenceRepository>() with singleton { DefaultPersistenceRepository(context = instance()) }
    bind<ScheduleWorkManagerService>() with singleton {
        DefaultScheduleWorkManagerService(workManager = instance())
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
    bind<DoorbellsDataSource>() with singleton { DefaultDoorbellsDataSource(doorbellRepository = instance()) }
    bind<DeviceInfoProvider>() with singleton { AndroidDeviceInfoProvider(context = instance()) }
    bind<IpAddressProvider>() with singleton { AndroidIpAddressProvider() }
    bind<ImagesDataSourceFactory>() with singleton { ImagesDataSourceFactoryImpl(cachedRepository = instance()) }
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
        KodeinFragmentFactory(activity, instance(arg = activity), dkodein)
    }

    // Permissions
    bind<Fragment>(tag = PermissionsFragment::class.simpleName) with factory { _: FragmentActivity, appNavigation: AppNavigation ->
        @Suppress("RedundantLambdaArrow") val fragment = PermissionsFragment { _: Bundle? ->
            instance(arg = appNavigation)
        }
        fragment
    }
    bind() from factory { appNavigation: AppNavigation ->
        PermissionsPresenter(appNavigation)
    }

    // DoorbellList
    bind<Fragment>(tag = DoorbellListFragment::class.simpleName) with factory { activity: FragmentActivity, appNavigation: AppNavigation ->
        DoorbellListFragment { instance(arg = M(activity, appNavigation)) }
    }
    bind<DoorbellListPresenter>() with factory { activity: FragmentActivity, appNavigation: AppNavigation ->
        val viewModelFactory: ViewModelProvider.Factory = instance()
        val viewModel = ViewModelProvider(activity, viewModelFactory)
            .get(DoorbellListViewModel::class.java)
        val thisDeviceRepository: ThisDeviceRepository = instance()
        DoorbellListPresenterImpl(
            doorbellListViewModel = viewModel,
            appNavigation = appNavigation,
            thisDeviceRepository = thisDeviceRepository
        )
    }

    // ImageList
    bind<Fragment>(tag = ImageListFragment::class.simpleName) with factory { _: FragmentActivity, appNavigation: AppNavigation ->
        ImageListFragment { args: Bundle? ->
            val appNavigationArgs: AppNavigationArgs = instance()
            val doorbellData = appNavigationArgs.getImagesFragmentArgs(args)
            instance(arg = M(appNavigation, doorbellData))
        }
    }
    bind<ImageListPresenter>() with factory { activity: FragmentActivity, appNavigation: AppNavigation, doorbellData: DoorbellData ->
        val viewModelFactory: ViewModelProvider.Factory = instance()
        val viewModel = ViewModelProvider(activity, viewModelFactory)
            .get(ImageListViewModel::class.java)
        ImageListPresenterImpl(
            doorbellData = doorbellData,
            imageListViewModel = viewModel,
            appNavigation = appNavigation
        )
    }

    // ImageDetails
    bind<Fragment>(tag = ImageDetailsFragment::class.simpleName) with factory { _: FragmentActivity, _: AppNavigation ->
        ImageDetailsFragment { args: Bundle? ->
            val appNavigationArgs: AppNavigationArgs = instance()
            val imageData = appNavigationArgs.getImageDetailsFragmentArgs(args)
            instance(arg = imageData)
        }
    }
    bind() from factory { imageData: ImageData ->
        ImageDetailsPresenterImpl(
            imageData = imageData
        )
    }
}

val viewModelModule = Kodein.Module(name = "viewModelModule") {
    bind<ViewModel>(tag = DoorbellListViewModel::class.simpleName) with provider {
        Timber.d("KodeinViewModelFactory:${DoorbellListViewModel::class.java.name}")
        DoorbellListViewModel(
            schedulerSet = instance(),
            doorbellRepository = instance(),
            thisDeviceRepository = instance(),
            cameraRepository = instance(),
            doorbellsDataSource = instance()
        )
    }

    bind<ViewModel>(tag = ImageListViewModel::class.simpleName) with provider {
        Timber.d("KodeinViewModelFactory:${ImageListViewModel::class.java.name}")
        ImageListViewModel(
            schedulerSet = instance(),
            doorbellRepository = instance(),
            cameraRepository = instance(),
            imagesDataSourceFactory = instance(),
            uptimeRepository = instance()
        )
    }
}
