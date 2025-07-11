package siarhei.luskanau.iot.doorbell.data.repository

import dev.gitlive.firebase.storage.File

class StubImageSenderRepository : ImageSenderRepository {
    override suspend fun sendImage(doorbellId: String, cameraId: String, file: File) = Unit
}
