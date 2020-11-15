package siarhei.luskanau.iot.doorbell.dagger.common

import android.app.Application
import android.content.Context
import androidx.startup.AppInitializer
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton
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

@Suppress("TooManyFunctions")
@Module
class CommonModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun provideWorkManager(context: Provider<Context>): WorkManager =
        AppInitializer.getInstance(context.get())
            .initializeComponent(WorkManagerInitializer::class.java)

    @Provides
    @Singleton
    fun provideImageRepository(context: Provider<Context>): ImageRepository =
        InternalStorageImageRepository(context = context.get())

    @Provides
    @Singleton
    fun provideDoorbellRepository(): DoorbellRepository {
        FirebaseDoorbellRepository()
        return StubDoorbellRepository()
    }

    @Provides
    @Singleton
    fun providePersistenceRepository(context: Provider<Context>): PersistenceRepository =
        DefaultPersistenceRepository(context = context.get())

    @Provides
    @Singleton
    fun provideScheduleWorkManagerService(
        workManager: Provider<WorkManager>
    ): ScheduleWorkManagerService =
        DefaultScheduleWorkManagerService(workManager = { workManager.get() })

    @Provides
    @Singleton
    fun provideCameraRepository(
        context: Provider<Context>,
        imageRepository: Provider<ImageRepository>
    ): CameraRepository =
        JetpackCameraRepository(
            context = context.get(),
            imageRepository = imageRepository.get()
        )

    @Provides
    @Singleton
    fun provideUptimeRepository(): UptimeRepository {
        UptimeFirebaseRepository()
        return StubUptimeRepository()
    }

    @Provides
    @Singleton
    fun provideDoorbellsDataSource(
        doorbellRepository: Provider<DoorbellRepository>
    ): DoorbellsDataSource =
        DefaultDoorbellsDataSource(
            doorbellRepository = doorbellRepository.get()
        )

    @Provides
    @Singleton
    fun provideDeviceInfoProvider(context: Provider<Context>): DeviceInfoProvider =
        AndroidDeviceInfoProvider(context = context.get())

    @Provides
    @Singleton
    fun provideIpAddressProvider(): IpAddressProvider =
        AndroidIpAddressProvider()

    @Provides
    @Singleton
    fun provideImagesDataSourceFactory(
        doorbellRepository: Provider<DoorbellRepository>
    ): ImagesDataSourceFactory =
        ImagesDataSourceFactoryImpl(
            doorbellRepository = doorbellRepository.get()
        )

    @Provides
    @Singleton
    fun provideThisDeviceRepository(
        context: Provider<Context>,
        deviceInfoProvider: Provider<DeviceInfoProvider>,
        cameraRepository: Provider<CameraRepository>,
        ipAddressProvider: Provider<IpAddressProvider>
    ): ThisDeviceRepository =
        AndroidThisDeviceRepository(
            context = context.get(),
            deviceInfoProvider = deviceInfoProvider.get(),
            cameraRepository = cameraRepository.get(),
            ipAddressProvider = ipAddressProvider.get()
        )

    @Provides
    @Singleton
    fun provideAppBackgroundServices(
        doorbellRepository: Provider<DoorbellRepository>,
        thisDeviceRepository: Provider<ThisDeviceRepository>,
        scheduleWorkManagerService: Provider<ScheduleWorkManagerService>
    ): AppBackgroundServices =
        AppBackgroundServices(
            doorbellRepository = doorbellRepository.get(),
            thisDeviceRepository = thisDeviceRepository.get(),
            scheduleWorkManagerService = scheduleWorkManagerService.get()
        )
}
