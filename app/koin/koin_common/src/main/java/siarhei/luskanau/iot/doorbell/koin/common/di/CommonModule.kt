package siarhei.luskanau.iot.doorbell.koin.common.di

import androidx.work.WorkManager
import org.koin.dsl.module
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
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepositoryFake
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.JetpackCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService

val commonModule = module {

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

    single<CameraRepository> {
        JetpackCameraRepository(
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
            doorbellRepository = get()
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
