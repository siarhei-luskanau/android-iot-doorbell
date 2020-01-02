package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface DoorbellRepository {

    suspend fun getDoorbellsList(
        size: Int,
        startAt: String? = null,
        orderAsc: Boolean = true
    ): List<DoorbellData>

    suspend fun getDoorbell(deviceId: String): DoorbellData?

    suspend fun getCamerasList(deviceId: String): List<CameraData>

    suspend fun sendDoorbellData(doorbellData: DoorbellData)

    suspend fun sendCamerasList(deviceId: String, list: List<CameraData>)

    suspend fun sendCameraImageRequest(deviceId: String, cameraId: String, isRequested: Boolean)

    suspend fun getCameraImageRequest(deviceId: String): Map<String, Boolean>

    suspend fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile)

    suspend fun getImagesList(
        deviceId: String,
        size: Int,
        imageIdAt: String? = null,
        orderAsc: Boolean = true
    ): List<ImageData>
}
