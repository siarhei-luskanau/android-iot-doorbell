package siarhei.luskanau.iot.doorbell.di

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.startup.AppInitializer
import org.koin.dsl.module
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
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.JetpackCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubImageSenderRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubUptimeRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.koin.common.di.KoinFragmentFactory
import siarhei.luskanau.iot.doorbell.koin.common.di.KoinViewModelFactory
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigation
import siarhei.luskanau.iot.doorbell.ui.splash.SplashNavigation
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerInitializer

val appModule = module {

    factory<AppNavigation> { (activity: FragmentActivity) -> DefaultAppNavigation(activity) }
    factory<SplashNavigation> { (activity: FragmentActivity) -> DefaultAppNavigation(activity) }

    factory<FragmentFactory> { (activity: FragmentActivity) ->
        KoinFragmentFactory(
            koin = getKoin(),
            activity = activity,
        )
    }

    factory<ViewModelProvider.Factory> { (
        activity: FragmentActivity,
        fragment: Fragment,
        args: Bundle?,
    ),
        ->
        KoinViewModelFactory(
            scope = this,
            activity = activity,
            fragment = fragment,
            args = args,
        )
    }

    single {
        AppInitializer.getInstance(get()).initializeComponent(WorkManagerInitializer::class.java)
    }

    single<ImageRepository> { InternalStorageImageRepository(context = get()) }

    single {
        if (get<ThisDeviceRepository>().isEmulator()) {
            StubDoorbellRepository()
        } else {
            FirebaseDoorbellRepository()
        }
    }

    single {
        if (get<ThisDeviceRepository>().isEmulator()) {
            StubImageSenderRepository()
        } else {
            FirebaseImageSenderRepository()
        }
    }

    single<ScheduleWorkManagerService> {
        DefaultScheduleWorkManagerService(
            workManager = { get() },
        )
    }

    single<CameraRepository> {
        JetpackCameraRepository(
            context = get(),
            imageRepository = get(),
        )
    }

    single {
        if (get<ThisDeviceRepository>().isEmulator()) {
            StubUptimeRepository()
        } else {
            UptimeFirebaseRepository()
        }
    }

    single<DoorbellsDataSource> {
        DefaultDoorbellsDataSource(
            doorbellRepository = get(),
        )
    }

    single<DeviceInfoProvider> { AndroidDeviceInfoProvider(context = get()) }

    single<IpAddressProvider> { AndroidIpAddressProvider() }

    single<ImagesDataSourceFactory> {
        ImagesDataSourceFactoryImpl(
            doorbellRepository = get(),
        )
    }

    single<ThisDeviceRepository> {
        AndroidThisDeviceRepository(
            context = get(),
            deviceInfoProvider = get(),
            cameraRepository = get(),
            ipAddressProvider = get(),
        )
    }

    single {
        AppBackgroundServices(
            doorbellRepository = get(),
            thisDeviceRepository = get(),
            scheduleWorkManagerService = get(),
        )
    }
}
