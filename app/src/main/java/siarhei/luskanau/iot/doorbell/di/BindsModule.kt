package siarhei.luskanau.iot.doorbell.di

import dagger.Binds
import dagger.Module
import siarhei.luskanau.iot.doorbell.data.model.camera.AndroidCameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.AndroidDoorbellDataBuilder
import siarhei.luskanau.iot.doorbell.data.model.device.DoorbellDataBuilder
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.AndroidIpAddressProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider

@Module
abstract class BindsModule {

    @Binds
    abstract fun bindDoorbellDataBuilder(bind: AndroidDoorbellDataBuilder): DoorbellDataBuilder

    @Binds
    abstract fun bindCameraDataProvider(bind: AndroidCameraDataProvider): CameraDataProvider

    @Binds
    abstract fun bindIpAddressProvider(bind: AndroidIpAddressProvider): IpAddressProvider

}