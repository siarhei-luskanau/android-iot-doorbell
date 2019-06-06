package siarhei.luskanau.iot.doorbell.dagger.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.cache.DefaultCachedRepository
import siarhei.luskanau.iot.doorbell.dagger.AppApplication
import siarhei.luskanau.iot.doorbell.dagger.di.viewmodel.ViewModelBinderModule
import siarhei.luskanau.iot.doorbell.dagger.di.viewmodel.ViewModelBuilderModule
import siarhei.luskanau.iot.doorbell.data.AndroidDeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.AndroidIpAddressProvider
import siarhei.luskanau.iot.doorbell.data.AndroidThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.DefaultSchedulerSet
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.AndroidCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.doomain.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.doomain.DefaultDoorbellsDataSource
import siarhei.luskanau.iot.doorbell.doomain.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.doomain.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.doomain.ImagesDataSourceFactoryImpl
import siarhei.luskanau.iot.doorbell.doomain.IpAddressProvider
import siarhei.luskanau.iot.doorbell.navigation.DefaultAppNavigationArgs
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultScheduleWorkManagerService
import javax.inject.Provider
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelBinderModule::class,
        ViewModelBuilderModule::class
    ]
)
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: AppApplication): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun provideSchedulerSet(): SchedulerSet =
        DefaultSchedulerSet()

    @Provides
    @Singleton
    fun provideWorkManager(context: Provider<Context>): WorkManager =
        WorkManager.getInstance(context.get())

    @Provides
    @Singleton
    fun provideAppNavigation(): AppNavigationArgs =
        DefaultAppNavigationArgs()

    @Provides
    @Singleton
    fun provideImageRepository(context: Provider<Context>): ImageRepository =
        InternalStorageImageRepository(context = context.get())

    @Provides
    @Singleton
    fun provideDoorbellRepository(imageRepository: Provider<ImageRepository>): DoorbellRepository =
        FirebaseDoorbellRepository(imageRepository = imageRepository.get())

    @Provides
    @Singleton
    fun providePersistenceRepository(context: Provider<Context>): PersistenceRepository =
        DefaultPersistenceRepository(context = context.get())

    @Provides
    @Singleton
    fun provideScheduleWorkManagerService(workManager: Provider<WorkManager>): ScheduleWorkManagerService =
        DefaultScheduleWorkManagerService(workManager = workManager.get())

    @Provides
    @Singleton
    fun provideCachedRepository(
        doorbellRepository: Provider<DoorbellRepository>,
        persistenceRepository: Provider<PersistenceRepository>
    ): CachedRepository =
        DefaultCachedRepository(
            doorbellRepository = doorbellRepository.get(),
            persistenceRepository = persistenceRepository.get()
        )

    @Provides
    @Singleton
    fun provideCameraRepository(
        context: Provider<Context>,
        imageRepository: Provider<ImageRepository>
    ): CameraRepository =
        AndroidCameraRepository(
            context = context.get(),
            imageRepository = imageRepository.get()
        )

    @Provides
    @Singleton
    fun provideUptimeRepository(): UptimeRepository =
        UptimeFirebaseRepository()

    @Provides
    @Singleton
    fun provideDoorbellsDataSource(doorbellRepository: Provider<DoorbellRepository>): DoorbellsDataSource =
        DefaultDoorbellsDataSource(doorbellRepository = doorbellRepository.get())

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
    fun provideImagesDataSourceFactory(cachedRepository: Provider<CachedRepository>): ImagesDataSourceFactory =
        ImagesDataSourceFactoryImpl(cachedRepository = cachedRepository.get())

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