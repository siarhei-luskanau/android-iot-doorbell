package siarhei.luskanau.iot.doorbell.data.repository

import dev.gitlive.firebase.database.ServerValue
import dev.gitlive.firebase.storage.File

class FirebaseImageSenderRepository :
    BaseFirebaseRepository(),
    ImageSenderRepository {

    override suspend fun sendImage(doorbellId: String, cameraId: String, file: File) {
        val imageId: String =
            getAppDatabase().child(
                FirebaseDoorbellRepository.IMAGES_KEY
            ).child(doorbellId).push().key.orEmpty()

        val storageRef = getAppStorage().child(imageId)
        storageRef.putFile(file)

        getAppDatabase().child(
            FirebaseDoorbellRepository.IMAGES_KEY
        ).child(doorbellId).child(imageId).setValue(
            ImageDto(
                imageId = imageId,
                imageStoragePath = storageRef.getDownloadUrl(),
                doorbellId = doorbellId,
                cameraId = cameraId,
                timestamp = 0
            )
        )

        getAppDatabase().child(
            FirebaseDoorbellRepository.IMAGES_KEY
        ).child(doorbellId).child(imageId).child("timestamp")
            .setValue(ServerValue.TIMESTAMP)
    }
}
