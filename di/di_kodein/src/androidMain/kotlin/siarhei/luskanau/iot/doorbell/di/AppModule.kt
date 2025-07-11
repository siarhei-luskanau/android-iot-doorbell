package siarhei.luskanau.iot.doorbell.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.startup.AppInitializer
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindSingleton
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton
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
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.JetpackCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubUptimeRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerInitializer

val appModule = DI.Module(name = "appModule") {
    bindSingleton {
        AppInitializer.getInstance(instance())
            .initializeComponent(WorkManagerInitializer::class.java)
    }
    bind<ImageRepository>() with singleton { InternalStorageImageRepository(context = instance()) }
    bind<DoorbellRepository>() with singleton {
        if (instance<ThisDeviceRepository>().isEmulator()) {
            StubDoorbellRepository()
        } else {
            FirebaseDoorbellRepository()
        }
    }
    bind<ImageSenderRepository>() with singleton {
        if (instance<ThisDeviceRepository>().isEmulator()) {
            StubImageSenderRepository()
        } else {
            FirebaseImageSenderRepository()
        }
    }
    bind<ScheduleWorkManagerService>() with singleton {
        DefaultScheduleWorkManagerService(workManager = { instance() })
    }
    bind<CameraRepository>() with singleton {
        JetpackCameraRepository(
            context = instance(),
            imageRepository = instance()
        )
    }
    bind<UptimeRepository>() with singleton {
        if (instance<ThisDeviceRepository>().isEmulator()) {
            StubUptimeRepository()
        } else {
            UptimeFirebaseRepository()
        }
    }
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
            doorbellRepository = instance()
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
    bindSingleton {
        AppBackgroundServices(
            doorbellRepository = instance(),
            thisDeviceRepository = instance(),
            scheduleWorkManagerService = instance()
        )
    }
}

val activityModule = DI.Module(name = "activityModule") {
    bind<FragmentFactory>() with factory { activity: FragmentActivity ->
        KodeinFragmentFactory(activity, directDI)
    }
    bind<ViewModelProvider.Factory>() with factory { viewModelFactoryArgs: ViewModelFactoryArgs ->
        KodeinViewModelFactory(
            injector = directDI,
            activity = viewModelFactoryArgs.activity,
            fragment = viewModelFactoryArgs.fragment,
            args = viewModelFactoryArgs.args
        )
    }

    // DoorbellList
    bind<Fragment>(
        tag = DoorbellListFragment::class.simpleName
    ) with factory { activity: FragmentActivity ->
        DoorbellListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                instance(
                    arg = ViewModelFactoryArgs(
                        activity = activity,
                        fragment = fragment,
                        args = fragment.arguments
                    )
                )
            ViewModelProvider(
                fragment as ViewModelStoreOwner,
                viewModelFactory
            )[DoorbellListViewModel::class.java]
        }
    }

    // ImageList
    bind<Fragment>(
        tag = ImageListFragment::class.simpleName
    ) with factory { activity: FragmentActivity ->
        ImageListFragment { fragment: Fragment ->
            val viewModelFactory: ViewModelProvider.Factory =
                instance(
                    arg = ViewModelFactoryArgs(
                        activity = activity,
                        fragment = fragment,
                        args = fragment.arguments
                    )
                )
            ViewModelProvider(fragment, viewModelFactory)[ImageListViewModel::class.java]
        }
    }

    // ImageDetails
    bind<Fragment>(
        tag = ImageDetailsFragment::class.simpleName
    ) with factory { activity: FragmentActivity ->
        ImageDetailsFragment { fragment: Fragment ->
            instance(
                arg = ViewModelFactoryArgs(
                    activity = activity,
                    fragment = fragment,
                    args = fragment.arguments
                )
            )
        }
    }
    bind {
        factory { viewModelFactoryArgs: ViewModelFactoryArgs ->
            val doorbellId = "doorbellId"
            val imageId = "imageId"
            ImageDetailsPresenterImpl(
                doorbellId = doorbellId,
                imageId = imageId,
                doorbellRepository = instance()
            )
        }
    }
}

val viewModelModule = DI.Module(name = "viewModelModule") {
    bind<ViewModel>(
        tag = DoorbellListViewModel::class.simpleName
    ) with factory { viewModelFactoryArgs: ViewModelFactoryArgs ->
        DoorbellListViewModel(
            appNavigation = instance(arg = viewModelFactoryArgs.activity),
            thisDeviceRepository = instance(),
            doorbellsDataSource = instance()
        )
    }

    bind<ViewModel>(
        tag = ImageListViewModel::class.simpleName
    ) with factory { viewModelFactoryArgs: ViewModelFactoryArgs ->
        val doorbellId = "doorbellId"
        ImageListViewModel(
            doorbellId = doorbellId,
            appNavigation = instance(arg = viewModelFactoryArgs.activity),
            doorbellRepository = instance(),
            imagesDataSourceFactory = instance()
        )
    }
}
