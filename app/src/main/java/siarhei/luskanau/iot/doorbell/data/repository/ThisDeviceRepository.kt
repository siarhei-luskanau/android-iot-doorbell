package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface ThisDeviceRepository {

    fun doorbellId(): String

    fun doorbellData(): DoorbellData

    fun getCamerasList(): List<CameraData>

    fun getIpAddressList(): List<Pair<String, String>>

}
