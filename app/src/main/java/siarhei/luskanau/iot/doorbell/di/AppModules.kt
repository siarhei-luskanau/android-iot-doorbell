package siarhei.luskanau.iot.doorbell.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import siarhei.luskanau.iot.doorbell.cache.DefaultCachedRepository
import siarhei.luskanau.iot.doorbell.common.AppNavigationArgs
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
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.CoroutineCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigationArgs
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService

class AppModules(application: Application) {

    val context: Context = application.applicationContext
    val appNavigationArgs: AppNavigationArgs by lazy { DefaultAppNavigationArgs() }
    val schedulerSet: SchedulerSet by lazy { DefaultSchedulerSet() }
    val imageRepository: ImageRepository by lazy {
        InternalStorageImageRepository(
            context = context
        )
    }
    val doorbellRepository: DoorbellRepository by lazy {
        FirebaseDoorbellRepository(imageRepository = imageRepository)
    }
    val uptimeRepository: UptimeRepository by lazy { UptimeFirebaseRepository() }
    val thisDeviceRepository: ThisDeviceRepository by lazy {
        AndroidThisDeviceRepository(
            context = context,
            deviceInfoProvider = deviceInfoProvider,
            cameraRepository = cameraRepository,
            ipAddressProvider = ipAddressProvider
        )
    }
    val deviceInfoProvider: DeviceInfoProvider by lazy {
        AndroidDeviceInfoProvider(
            context = context
        )
    }
    val cameraRepository: CameraRepository by lazy {
        CoroutineCameraRepository(
            context = context,
            imageRepository = imageRepository
        )
    }
    val persistenceRepository: PersistenceRepository by lazy {
        DefaultPersistenceRepository(
            context = context
        )
    }
    val cachedRepository: CachedRepository by lazy {
        DefaultCachedRepository(
            doorbellRepository = doorbellRepository,
            persistenceRepository = persistenceRepository
        )
    }
    val imagesDataSourceFactory: ImagesDataSourceFactory by lazy {
        ImagesDataSourceFactoryImpl(
            cachedRepository = cachedRepository
        )
    }
    val ipAddressProvider: IpAddressProvider by lazy { AndroidIpAddressProvider() }
    val doorbellsDataSource: DoorbellsDataSource by lazy {
        DefaultDoorbellsDataSource(
            doorbellRepository = doorbellRepository
        )
    }
    val workManager: WorkManager by lazy { WorkManager.getInstance(context) }
    val scheduleWorkManagerService: ScheduleWorkManagerService by lazy {
        DefaultScheduleWorkManagerService(workManager = { workManager })
    }
    val appBackgroundServices: AppBackgroundServices by lazy {
        AppBackgroundServices(
            doorbellRepository = doorbellRepository,
            thisDeviceRepository = thisDeviceRepository,
            scheduleWorkManagerService = scheduleWorkManagerService
        )
    }
}