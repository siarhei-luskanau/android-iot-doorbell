package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface DoorbellRepository {

    fun getDoorbellsList(size: Int, startAt: String? = null, orderAsc: Boolean = true): List<DoorbellData>

    fun getCamerasList(deviceId: String): List<CameraData>

    fun sendDoorbellData(doorbellData: DoorbellData)

    fun sendCamerasList(deviceId: String, list: List<CameraData>)

    fun sendCameraImageRequest(deviceId: String, cameraId: String, isRequested: Boolean)

    fun getCameraImageRequest(deviceId: String): Map<String, Boolean>

    fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile)

    fun getImagesList(deviceId: String, size: Int, imageIdAt: String? = null, orderAsc: Boolean = true): List<ImageData>
}