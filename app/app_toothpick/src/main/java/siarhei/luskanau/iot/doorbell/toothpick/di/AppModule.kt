package siarhei.luskanau.iot.doorbell.toothpick.di

import android.app.Application
import android.content.Context
import androidx.startup.AppInitializer
import androidx.work.WorkManager
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
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.JetpackCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubUptimeRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerInitializer
import toothpick.config.Module

class AppModule(application: Application) : Module() {

    init {
        val context = application.applicationContext
        bind(Application::class.java).toProviderInstance { application }
        bind(Context::class.java).toProviderInstance { context }

        val imageRepository: ImageRepository by lazy {
            InternalStorageImageRepository(
                context = context
            )
        }
        bind(ImageRepository::class.java).toProviderInstance { imageRepository }

        val doorbellRepository: DoorbellRepository by lazy {
            FirebaseDoorbellRepository()
            StubDoorbellRepository()
        }
        bind(DoorbellRepository::class.java).toProviderInstance { doorbellRepository }

        val uptimeRepository: UptimeRepository by lazy {
            UptimeFirebaseRepository()
            StubUptimeRepository()
        }
        bind(UptimeRepository::class.java).toProviderInstance { uptimeRepository }

        val deviceInfoProvider: DeviceInfoProvider by lazy {
            AndroidDeviceInfoProvider(
                context = context
            )
        }
        bind(DeviceInfoProvider::class.java).toProviderInstance { deviceInfoProvider }

        val cameraRepository: CameraRepository by lazy {
            JetpackCameraRepository(
                context = context,
                imageRepository = imageRepository
            )
        }
        bind(CameraRepository::class.java).toProviderInstance { cameraRepository }

        val ipAddressProvider: IpAddressProvider by lazy { AndroidIpAddressProvider() }
        bind(IpAddressProvider::class.java).toProviderInstance { ipAddressProvider }

        val thisDeviceRepository: ThisDeviceRepository by lazy {
            AndroidThisDeviceRepository(
                context = context,
                deviceInfoProvider = deviceInfoProvider,
                cameraRepository = cameraRepository,
                ipAddressProvider = ipAddressProvider
            )
        }
        bind(ThisDeviceRepository::class.java).toProviderInstance { thisDeviceRepository }

        val persistenceRepository: PersistenceRepository by lazy {
            DefaultPersistenceRepository(
                context = context
            )
        }
        bind(PersistenceRepository::class.java).toProviderInstance { persistenceRepository }

        val imagesDataSourceFactory: ImagesDataSourceFactory by lazy {
            ImagesDataSourceFactoryImpl(
                doorbellRepository = doorbellRepository
            )
        }
        bind(ImagesDataSourceFactory::class.java).toProviderInstance { imagesDataSourceFactory }

        val doorbellsDataSource: DoorbellsDataSource by lazy {
            DefaultDoorbellsDataSource(
                doorbellRepository = doorbellRepository
            )
        }
        bind(DoorbellsDataSource::class.java).toProviderInstance { doorbellsDataSource }

        val workManager: WorkManager by lazy {
            AppInitializer.getInstance(context)
                .initializeComponent(WorkManagerInitializer::class.java)
        }
        bind(WorkManager::class.java).toProviderInstance { workManager }

        val scheduleWorkManagerService: ScheduleWorkManagerService by lazy {
            DefaultScheduleWorkManagerService(workManager = { workManager })
        }
        bind(ScheduleWorkManagerService::class.java).toProviderInstance {
            scheduleWorkManagerService
        }

        val appBackgroundServices: AppBackgroundServices by lazy {
            AppBackgroundServices(
                doorbellRepository = doorbellRepository,
                thisDeviceRepository = thisDeviceRepository,
                scheduleWorkManagerService = scheduleWorkManagerService
            )
        }
        bind(AppBackgroundServices::class.java).toProviderInstance { appBackgroundServices }
    }
}
