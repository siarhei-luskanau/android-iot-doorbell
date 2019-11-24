package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.moshi.Moshi
import java.io.InputStream
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

open class BaseFirebaseRepository(
    private val moshi: Moshi = Moshi.Builder().build()
) {

    companion object {
        protected const val DOORBELL_APP_KEY = "doorbell_app"
    }

    protected fun serializeByMoshi(src: Any?): Any? =
        moshi.adapter(Any::class.java).fromJsonValue(
            moshi.adapter<Any>(Object::class.java).toJsonValue(src)
        )

    protected fun getAppDatabase(): DatabaseReference =
        FirebaseDatabase.getInstance().getReference(DOORBELL_APP_KEY)

    protected fun getAppStorage(): StorageReference =
        FirebaseStorage.getInstance().getReference(DOORBELL_APP_KEY)

    protected fun <T : Any> dataSnapshotObject(dataSnapshot: DataSnapshot, type: Class<T>): T? =
        moshi.adapter<Any>(Object::class.java).toJson(dataSnapshot.value)?.let { json ->
            moshi.adapter(type).fromJson(json)
        }

    protected fun <T : Any> dataSnapshotToList(
        dataSnapshot: DataSnapshot,
        type: Class<T>
    ): List<T> =
        dataSnapshot.children.mapNotNull {
            moshi.adapter<Any>(Object::class.java).toJson(it.value)?.let { json ->
                moshi.adapter(type).fromJson(json)
            }
        }

    protected fun <T : Any> dataSnapshotToMap(
        dataSnapshot: DataSnapshot,
        type: Class<T>
    ): Map<String, T> =
        // if (dataSnapshot.exists())
        dataSnapshot.children.associateBy(
            { it.key.orEmpty() },
            {
                moshi.adapter<Any>(Object::class.java).toJson(it.value)?.let { json ->
                    moshi.adapter(type).fromJson(json)
                }
            }
        )
            .filterValues { it != null }
            .mapValues { entry -> entry.value as T }

    protected suspend fun putStreamToStorage(
        storageRef: StorageReference,
        stream: InputStream
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
