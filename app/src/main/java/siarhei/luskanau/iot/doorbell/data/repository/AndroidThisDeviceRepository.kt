package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider
import timber.log.Timber
import javax.inject.Inject

class AndroidThisDeviceRepository @Inject constructor(
        private val deviceInfoProvider: DeviceInfoProvider,
        private val cameraDataProvider: CameraDataProvider,
        private val ipAddressProvider: IpAddressProvider
) : ThisDeviceRepository {

    private val doorbellData = DoorbellData(
            deviceInfoProvider.buildDeviceId(),
            deviceInfoProvider.buildDeviceName(),
            deviceInfoProvider.isAndroidThings(),
            deviceInfoProvider.buildDeviceInfo()
    )

    override fun doorbellId() = deviceInfoProvider.buildDeviceId()

    override fun doorbellData() = doorbellData

    override fun getCamerasList() = cameraDataProvider.getCamerasList()

    override fun getIpAddressList() = ipAddressProvider.getIpAddressList()

    override fun reboot() {
        try {
            Timber.d("reboot")
            Runtime.getRuntime().exec("reboot")
        } catch (t: Throwable) {
            Timber.e(t)
        }
    }

}
