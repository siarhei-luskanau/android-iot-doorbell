package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import java.io.InputStream

interface DoorbellRepository {

    suspend fun getDoorbellsList(
        size: Int,
        startAt: String? = null,
        orderAsc: Boolean = true
    ): List<DoorbellData>

    suspend fun getDoorbell(doorbellId: String): DoorbellData?

    suspend fun getCamerasList(doorbellId: String): List<CameraData>

    suspend fun sendDoorbellData(doorbellData: DoorbellData)

    suspend fun sendCamerasList(doorbellId: String, list: List<CameraData>)

    suspend fun sendCameraImageRequest(doorbellId: String, cameraId: String, isRequested: Boolean)

    suspend fun getCameraImageRequest(doorbellId: String): Map<String, Boolean>

    suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        imageInputStream: InputStream
    )

    suspend fun getImagesList(
        doorbellId: String,
        size: Int,
        imageIdAt: String? = null,
        orderAsc: Boolean = true
    ): List<ImageData>

    suspend fun getImage(
        doorbellId: String,
        imageId: String
    ): ImageData?
}
