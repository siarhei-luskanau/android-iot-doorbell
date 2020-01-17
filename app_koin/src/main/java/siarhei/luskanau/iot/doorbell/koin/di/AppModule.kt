package siarhei.luskanau.iot.doorbell.koin.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
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
import siarhei.luskanau.iot.doorbell.data.DefaultSchedulerSet
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.CoroutineCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepositoryFake
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenter
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListViewModel
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenter
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenter
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import timber.log.Timber

val appModule = module {
    single<SchedulerSet> { DefaultSchedulerSet() }
    single { WorkManager.getInstance(get()) }
    single<ImageRepository> { InternalStorageImageRepository(context = get()) }
    single<DoorbellRepository> {
        FirebaseDoorbellRepository(imageRepository = get())
        DoorbellRepositoryFake()
    }
    single<PersistenceRepository> { DefaultPersistenceRepository(context = get()) }
    single<ScheduleWorkManagerService> {
        DefaultScheduleWorkManagerService(
            workManager = { get() }
        )
    }
    single<CachedRepository> {
        DefaultCachedRepository(
            doorbellRepository = get(),
            persistenceRepository = get()
        )
    }
    single<CameraRepository> {
        CoroutineCameraRepository(
            context = get(),
            imageRepository = get()
        )
    }
    single<UptimeRepository> { UptimeFirebaseRepository() }
    single<DoorbellsDataSource> {
        DefaultDoorbellsDataSource(
            doorbellRepository = get()
        )
    }
    single<DeviceInfoProvider> { AndroidDeviceInfoProvider(context = get()) }
    single<IpAddressProvider> { AndroidIpAddressProvider() }
    single<ImagesDataSourceFactory> {
        ImagesDataSourceFactoryImpl(
            cachedRepository = get()
        )
    }
    single<ThisDeviceRepository> {
        AndroidThisDeviceRepository(
            context = get(),
            deviceInfoProvider = get(),
            cameraRepository = get(),
            ipAddressProvider = get()
        )
    }
    single {
        AppBackgroundServices(
            doorbellRepository = get(),
            thisDeviceRepository = get(),
            scheduleWorkManagerService = get()
        )
    }
}

val activityModule = module {
    factory<AppNavigation> { (activity: FragmentActivity) -> DefaultAppNavigation(activity) }
    factory<FragmentFactory> { (activity: FragmentActivity) ->
        val appNavigation: AppNavigation = get { parametersOf(activity) }
        KoinFragmentFactory(appNavigation = appNavigation)
    }

    // Permissions
    factory { (appNavigation: AppNavigation) ->
        PermissionsFragment { get { parametersOf(appNavigation) } }
    }
    factory { (appNavigation: AppNavigation) ->
        PermissionsPresenter(appNavigation)
    }

    // DoorbellList
    factory { (appNavigation: AppNavigation) ->
        DoorbellListFragment { fragment: Fragment ->
            val thisDeviceRepository: ThisDeviceRepository = get()
            get {
                parametersOf(
                    fragment,
                    appNavigation,
                    thisDeviceRepository
                )
            }
        }
    }
    factory<DoorbellListPresenter> { (
                                         lifecycleOwner: LifecycleOwner,
                                         appNavigation: AppNavigation,
                                         thisDeviceRepository: ThisDeviceRepository
                                     ) ->
        DoorbellListPresenterImpl(
            doorbellListViewModel = lifecycleOwner.getViewModel(),
            appNavigation = appNavigation,
            thisDeviceRepository = thisDeviceRepository
        )
    }

    // ImageList
    factory { (appNavigation: AppNavigation) ->
        ImageListFragment { fragment: Fragment ->
            val doorbellData = fragment.arguments?.let { args: Bundle ->
                ImageListFragmentArgs.fromBundle(args).doorbellData
            }
            get {
                parametersOf(
                    doorbellData,
                    fragment,
                    appNavigation
                )
            }
        }
    }
    factory<ImageListPresenter> { (
                                      doorbellData: DoorbellData,
                                      lifecycleOwner: LifecycleOwner,
                                      appNavigation: AppNavigation
                                  ) ->
        ImageListPresenterImpl(
            doorbellData = doorbellData,
            imageListViewModel = lifecycleOwner.getViewModel(),
            appNavigation = appNavigation
        )
    }

    // ImageDetails
    factory { (appNavigation: AppNavigation) ->
        ImageDetailsFragment { fragment: Fragment ->
            val doorbellData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).doorbellData
            }
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            get { parametersOf(appNavigation, fragment, doorbellData, imageData) }
        }
    }
    factory<ImageDetailsPresenter> { (
                                         appNavigation: AppNavigation,
                                         fragment: Fragment,
                                         doorbellData: DoorbellData,
                                         imageData: ImageData
                                     ) ->
        ImageDetailsPresenterImpl(
            appNavigation = appNavigation,
            fragment = fragment,
            doorbellData = doorbellData,
            imageData = imageData
        )
    }

    // ImageDetailsSlide
    factory { (_: AppNavigation) ->
        ImageDetailsSlideFragment { fragment: Fragment ->
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            get { parametersOf(imageData) }
        }
    }
    factory<ImageDetailsSlidePresenter> { (imageData: ImageData) ->
        val imageDetailsSlidePresenterImpl = ImageDetailsSlidePresenterImpl(
            imageData = imageData
        )
        imageDetailsSlidePresenterImpl
    }
}

val viewModelModule = module {
    viewModel {
        Timber.d("KoinViewModelFactory:${DoorbellListViewModel::class.java.name}")
        DoorbellListViewModel(
            schedulerSet = get(),
            doorbellRepository = get(),
            thisDeviceRepository = get(),
            cameraRepository = get(),
            doorbellsDataSource = get()
        )
    }
    viewModel {
        Timber.d("KoinViewModelFactory:${ImageListViewModel::class.java.name}")
        ImageListViewModel(
            schedulerSet = get(),
            doorbellRepository = get(),
            imagesDataSourceFactory = get(),
            uptimeRepository = get()
        )
    }
}
