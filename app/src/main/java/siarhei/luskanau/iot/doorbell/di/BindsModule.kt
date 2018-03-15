package siarhei.luskanau.iot.doorbell.di

import dagger.Binds
import dagger.Module
import siarhei.luskanau.iot.doorbell.data.model.camera.AndroidCameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.AndroidDeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.AndroidIpAddressProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider
import siarhei.luskanau.iot.doorbell.data.repository.AndroidThisDeviceRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import javax.inject.Singleton

@Module
abstract class BindsModule {

    @Binds
    abstract fun bindDeviceInfoProvider(bind: AndroidDeviceInfoProvider): DeviceInfoProvider

    @Binds
    abstract fun bindCameraDataProvider(bind: AndroidCameraDataProvider): CameraDataProvider

    @Binds
    abstract fun bindIpAddressProvider(bind: AndroidIpAddressProvider): IpAddressProvider

    @Binds
    @Singleton
    abstract fun bindThisDeviceRepository(bind: AndroidThisDeviceRepository): ThisDeviceRepository

}