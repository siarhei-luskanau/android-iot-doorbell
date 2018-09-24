package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import java.io.InputStream
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

abstract class BaseFirebaseRepository(private val gson: Gson = Gson()) {

    companion object {
        protected const val DOORBELL_APP_KEY = "doorbell_app"
    }

    protected fun serializeByGson(src: Any?): Any? =
            gson.fromJson(gson.toJson(src), Object::class.java)

    protected fun getAppDatabase(): DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DOORBELL_APP_KEY)

    protected fun getAppStorage(): StorageReference =
            FirebaseStorage.getInstance().getReference(DOORBELL_APP_KEY)

    protected fun <T> dataSnapshotObject(dataSnapshot: DataSnapshot, type: Class<T>): T =
            gson.fromJson(gson.toJson(dataSnapshot.value), type)

    protected fun <T> dataSnapshotToList(dataSnapshot: DataSnapshot, type: Class<T>): List<T> =
            dataSnapshot.children.map { gson.fromJson(gson.toJson(it.value), type) }

    protected fun <T> dataSnapshotToMap(dataSnapshot: DataSnapshot, type: Class<T>): Map<String, T> =
    //if (dataSnapshot.exists())
            dataSnapshot.children.associateBy(
                    { it.key.orEmpty() },
                    { gson.fromJson(gson.toJson(it.value), type) }
            )

    protected suspend fun putStreamToStorage(storageRef: StorageReference, stream: InputStream): Uri? =
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

    protected suspend fun setValueToDatabase(ref: DatabaseReference, value: Any?) =
            suspendCoroutine { continuation: Continuation<Unit> ->
                ref.setValue(value)
                        .addOnSuccessListener {
                            continuation.resume(Unit)
                        }
                        .addOnFailureListener {
                            continuation.resumeWithException(it)
                        }
            }

    protected suspend fun getValueFromDatabase(query: Query): DataSnapshot =
            suspendCoroutine { continuation: Continuation<DataSnapshot> ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        continuation.resume(dataSnapshot)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        continuation.resumeWithException(databaseError.toException())
                    }
                })
            }

}