package siarhei.luskanau.iot.doorbell.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.*
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DefaultDoorbellsDataSource
import siarhei.luskanau.iot.doorbell.datasource.doorbells.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.datasource.images.DefaultImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.datasource.images.ImagesDataSourceFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class, BindsModule::class])
class AppModule {

    @Provides
    fun provideContext(application: AppApplication): Context =
            application.applicationContext

    @Provides
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSchedulerSet(): SchedulerSet =
            SchedulerSet()

    @Provides
    @Singleton
    fun provideImageRepository(context: Context): ImageRepository =
            InternalStorageImageRepository(context)

    @Provides
    @Singleton
    fun provideDoorbellRepository(
            gson: Gson,
            imageRepository: ImageRepository
    ): DoorbellRepository =
            FirebaseDoorbellRepository(
                    gson,
                    imageRepository
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
    fun provideUptimeRepository(gson: Gson): UptimeRepository =
            UptimeFirebaseRepository(gson)

    @Provides
    @Singleton
    fun provideDoorbellsDataSource(
            schedulerSet: SchedulerSet,
            doorbellRepository: DoorbellRepository
    ): DoorbellsDataSource = DefaultDoorbellsDataSource(
            schedulerSet,
            doorbellRepository
    )

    @Provides
    @Singleton
    fun provideImagesDataSourceFactory(
            schedulerSet: SchedulerSet,
            doorbellRepository: DoorbellRepository
    ): ImagesDataSourceFactory = DefaultImagesDataSourceFactory(
            schedulerSet,
            doorbellRepository
    )

}