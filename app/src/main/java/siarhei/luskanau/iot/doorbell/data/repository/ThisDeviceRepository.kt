package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData

interface ThisDeviceRepository {

    fun doorbellId(): String

    fun doorbellData(): DoorbellData

    fun getCamerasList(): Single<List<CameraData>>

    fun getIpAddressMap(): Single<Map<String, String>>

}
