package siarhei.luskanau.iot.doorbell.data.repository

import java.io.InputStream

interface ImageSenderRepository {
    suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        imageInputStream: InputStream,
    )
}
