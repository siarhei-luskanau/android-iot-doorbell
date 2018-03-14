package siarhei.luskanau.iot.doorbell.di

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.AppApplication
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DoorbellDataBuilder
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.repository.AndroidThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.FirebaseDoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
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
    fun provideThisDeviceRepository(
            doorbellDataBuilder: DoorbellDataBuilder,
            cameraDataProvider: CameraDataProvider,
            ipAddressProvider: IpAddressProvider
    ): ThisDeviceRepository =
            AndroidThisDeviceRepository(
                    doorbellDataBuilder,
                    cameraDataProvider,
                    ipAddressProvider
            )

}