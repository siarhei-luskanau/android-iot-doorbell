package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface CameraRepository {

    suspend fun makeImage(
        deviceId: String,
        cameraId: String
    ): ImageFile
}