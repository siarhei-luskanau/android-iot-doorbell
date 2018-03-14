package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.camera.CameraDataProvider
import siarhei.luskanau.iot.doorbell.data.model.device.DoorbellDataBuilder
import siarhei.luskanau.iot.doorbell.data.model.ipaddress.IpAddressProvider

class AndroidThisDeviceRepository(
        doorbellDataBuilder: DoorbellDataBuilder,
        private val cameraDataProvider: CameraDataProvider,
        private val ipAddressProvider: IpAddressProvider
) : ThisDeviceRepository {

    private val doorbellData = doorbellDataBuilder.buildDoorbellData()

    override fun doorbellId() = doorbellData.doorbellId

    override fun doorbellData() = doorbellData

    override fun getCamerasList(): List<CameraData> = cameraDataProvider.getCamerasList()

    override fun getIpAddressList(): List<Pair<String, String>> = ipAddressProvider.getIpAddressList()

}
