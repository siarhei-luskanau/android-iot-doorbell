package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface ThisDeviceRepository {

    fun isEmulator(): Boolean

    suspend fun doorbellId(): String

    suspend fun doorbellData(): DoorbellData

    suspend fun getCamerasList(): List<CameraData>

    suspend fun getIpAddressList(): Map<String, Pair<String, String>>

    fun reboot()

    fun isPermissionsGranted(): Boolean
}
