package siarhei.luskanau.iot.doorbell.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.cache.DefaultCachedRepository
import siarhei.luskanau.iot.doorbell.data.DefaultSchedulerSet
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.UptimeService
import siarhei.luskanau.iot.doorbell.data.repository.AndroidCameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeFirebaseRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DefaultDoorbellsDataSource
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.datasource.images.DefaultImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.datasource.images.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.persistence.DefaultPersistenceRepository
import siarhei.luskanau.iot.doorbell.workmanager.DefaultUptimeService
import javax.inject.Singleton

@Module(includes = [
    ViewModelModule::class,
    BindsModule::class
])
class AppModule {

    @Provides
    fun provideContext(application: AppApplication): Context =
            application.applicationContext

    @Provides
    @Singleton
    fun provideWorkManager(): WorkManager = WorkManager.getInstance()

    @Provides
    @Singleton
    fun provideSchedulerSet(): SchedulerSet =
            DefaultSchedulerSet()

    @Provides
    @Singleton
    fun provideImageRepository(context: Context): ImageRepository =
            InternalStorageImageRepository(context)

    @Provides
    @Singleton
    fun provideDoorbellRepository(
        imageRepository: ImageRepository
    ): DoorbellRepository =
            FirebaseDoorbellRepository(
                    imageRepository
            )

    @Provides
    @Singleton
    fun providePersistenceRepository(context: Context): PersistenceRepository =
            DefaultPersistenceRepository(context)

    @Provides
    @Singleton
    fun provideUptimeService(
        workManager: WorkManager
    ): UptimeService =
            DefaultUptimeService(workManager)

    @Provides
    @Singleton
    fun provideCachedRepository(
        doorbellRepository: DoorbellRepository,
        persistenceRepository: PersistenceRepository
    ): CachedRepository =
            DefaultCachedRepository(
                    doorbellRepository,
                    persistenceRepository
            )

    @Provides
    @Singleton
    fun provideCameraRepository(
        context: Context,
        imageRepository: ImageRepository
    ): CameraRepository = AndroidCameraRepository(
            context,
            imageRepository
    )

    @Provides
    @Singleton
    fun provideUptimeRepository(): UptimeRepository =
            UptimeFirebaseRepository()

    @Provides
    @Singleton
    fun provideDoorbellsDataSource(
        doorbellRepository: DoorbellRepository
    ): DoorbellsDataSource = DefaultDoorbellsDataSource(
            doorbellRepository
    )

    @Provides
    @Singleton
    fun provideImagesDataSourceFactory(cachedRepository: CachedRepository): ImagesDataSourceFactory =
            DefaultImagesDataSourceFactory(cachedRepository)
}