package siarhei.luskanau.iot.doorbell.di

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
import siarhei.luskanau.iot.doorbell.data.repository.StubDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.StubUptimeRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.workmanager.WorkManagerInitializer

class AppModules(context: Context) {

    val imageRepository: ImageRepository by lazy {
        InternalStorageImageRepository(
            context = context,
        )
    }
    val doorbellRepository: DoorbellRepository by lazy {
        if (thisDeviceRepository.isEmulator()) {
            StubDoorbellRepository()
        } else {
            FirebaseDoorbellRepository()
        }
    }
    val uptimeRepository: UptimeRepository by lazy {
        if (thisDeviceRepository.isEmulator()) {
            StubUptimeRepository()
        } else {
            UptimeFirebaseRepository()
        }
    }
    val thisDeviceRepository: ThisDeviceRepository by lazy {
        AndroidThisDeviceRepository(
            context = context,
            deviceInfoProvider = deviceInfoProvider,
            cameraRepository = cameraRepository,
            ipAddressProvider = ipAddressProvider,
        )
    }
    val deviceInfoProvider: DeviceInfoProvider by lazy {
        AndroidDeviceInfoProvider(
            context = context,
        )
    }
    val cameraRepository: CameraRepository by lazy {
        JetpackCameraRepository(
            context = context,
            imageRepository = imageRepository,
        )
    }
    val imagesDataSourceFactory: ImagesDataSourceFactory by lazy {
        ImagesDataSourceFactoryImpl(
            doorbellRepository = doorbellRepository,
        )
    }
    val ipAddressProvider: IpAddressProvider by lazy { AndroidIpAddressProvider() }
    val doorbellsDataSource: DoorbellsDataSource by lazy {
        DefaultDoorbellsDataSource(
            doorbellRepository = doorbellRepository,
        )
    }
    val workManager: WorkManager by lazy {
        AppInitializer.getInstance(context).initializeComponent(WorkManagerInitializer::class.java)
    }
    val scheduleWorkManagerService: ScheduleWorkManagerService by lazy {
        DefaultScheduleWorkManagerService(workManager = { workManager })
    }
    val appBackgroundServices: AppBackgroundServices by lazy {
        AppBackgroundServices(
            doorbellRepository = doorbellRepository,
            thisDeviceRepository = thisDeviceRepository,
            scheduleWorkManagerService = scheduleWorkManagerService,
        )
    }
}
