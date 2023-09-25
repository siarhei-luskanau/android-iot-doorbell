package siarhei.luskanau.iot.doorbell.data.repository

import dev.gitlive.firebase.storage.File

interface ImageSenderRepository {
    suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        file: File,
    )
}
