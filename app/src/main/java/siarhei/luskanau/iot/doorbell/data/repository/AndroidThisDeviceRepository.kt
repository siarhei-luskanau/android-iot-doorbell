package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider
import javax.inject.Inject

class AndroidThisDeviceRepository @Inject constructor(
        private val deviceInfoProvider: DeviceInfoProvider,
        private val cameraDataProvider: CameraDataProvider,
        private val ipAddressProvider: IpAddressProvider
) : ThisDeviceRepository {

    private val doorbellData = DoorbellData(
            deviceInfoProvider.buildDeviceId(),
            deviceInfoProvider.buildDeviceName(),
            deviceInfoProvider.buildDeviceInfo()
    )

    override fun doorbellId() = deviceInfoProvider.buildDeviceId()

    override fun doorbellData() = doorbellData

    override fun getCamerasList() = cameraDataProvider.getCamerasList()

    override fun getIpAddressList() = ipAddressProvider.getIpAddressList()

}
