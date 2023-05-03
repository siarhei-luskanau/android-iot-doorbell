package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.InputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseImageSenderRepository : BaseFirebaseRepository(), ImageSenderRepository {

    private fun getAppStorage(): StorageReference =
        Firebase.storage.getReference(DOORBELL_APP_KEY)

    override suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        imageInputStream: InputStream,
    ) {
        val imageId: String =
            getAppDatabase().child(FirebaseDoorbellRepository.IMAGES_KEY).child(doorbellId).push().key.orEmpty()

        val uri: Uri? = putStreamToStorage(
            getAppStorage().child(imageId),
            imageInputStream,
        )

        getAppDatabase().child(FirebaseDoorbellRepository.IMAGES_KEY).child(doorbellId).child(imageId).setValue(
            ImageDto(
                imageId = imageId,
                imageStoragePath = uri.toString(),
                doorbellId = doorbellId,
                cameraId = cameraId,
                timestamp = 0,
            ),
        )

        getAppDatabase().child(FirebaseDoorbellRepository.IMAGES_KEY).child(doorbellId).child(imageId).child("timestamp")
            .setValue(ServerValue.TIMESTAMP)
    }

    private suspend fun putStreamToStorage(
        storageRef: StorageReference,
        stream: InputStream,
    ): Uri? =
        suspendCoroutine { continuation ->
            storageRef.putStream(stream)
                .continueWithTask {
                    storageRef.downloadUrl
                }
                .addOnCompleteListener {
                    continuation.resume(it.result)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
}
