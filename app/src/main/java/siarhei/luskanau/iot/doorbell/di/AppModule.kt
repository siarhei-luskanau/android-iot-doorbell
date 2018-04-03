package siarhei.luskanau.iot.doorbell.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.repository.*
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
    fun provideDoorbellRepository(gson: Gson): DoorbellRepository =
            FirebaseDoorbellRepository(gson)

    @Provides
    @Singleton
    fun provideCameraRepository(context: Context): CameraRepository =
            AndroidCameraRepository(context)

    @Provides
    @Singleton
    fun provideUptimeRepository(gson: Gson): UptimeRepository =
            UptimeFirebaseRepository(gson)

}