package siarhei.luskanau.iot.doorbell.data.repository

import java.io.InputStream

class StubImageSenderRepository : ImageSenderRepository {
    override suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        imageInputStream: InputStream,
    ) = Unit
}
