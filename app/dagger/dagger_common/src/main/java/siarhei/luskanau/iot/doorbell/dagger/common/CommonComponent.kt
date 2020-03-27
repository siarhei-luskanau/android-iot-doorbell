package siarhei.luskanau.iot.doorbell.dagger.common

import android.app.Application
import androidx.work.WorkManager
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.common.DoorbellsDataSource
import siarhei.luskanau.iot.doorbell.common.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.common.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.AppBackgroundServices
import siarhei.luskanau.iot.doorbell.data.ScheduleWorkManagerService
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.UptimeRepository

@Singleton
@Component(modules = [CommonModule::class])
interface CommonComponent {

    fun provideWorkManager(): WorkManager
    fun provideImageRepository(): ImageRepository
    fun provideDoorbellRepository(): DoorbellRepository
    fun providePersistenceRepository(): PersistenceRepository
    fun provideScheduleWorkManagerService(): ScheduleWorkManagerService
    fun provideCachedRepository(): CachedRepository
    fun provideCameraRepository(): CameraRepository
    fun provideUptimeRepository(): UptimeRepository
    fun provideDoorbellsDataSource(): DoorbellsDataSource
    fun provideDeviceInfoProvider(): DeviceInfoProvider
    fun provideIpAddressProvider(): IpAddressProvider
    fun provideImagesDataSourceFactory(): ImagesDataSourceFactory
    fun provideThisDeviceRepository(): ThisDeviceRepository
    fun provideAppBackgroundServices(): AppBackgroundServices

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): CommonComponent
    }
}
