package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStream
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Suppress("TooManyFunctions")
open class BaseFirebaseRepository {

    protected val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    protected val gson: Gson by lazy {
        GsonBuilder()
            .serializeNulls()
            .create()
    }

    companion object {
        protected const val DOORBELL_APP_KEY = "doorbell_app"
    }

    protected fun getAppDatabase(): DatabaseReference =
        Firebase.database.getReference(DOORBELL_APP_KEY)

    protected fun getAppStorage(): StorageReference =
        Firebase.storage.getReference(DOORBELL_APP_KEY)

    protected inline fun <reified T> dataSnapshotObject(dataSnapshot: DataSnapshot): T? =
        (dataSnapshot.value as? Map<*, *>)?.toJsonObject().let { jsonObject ->
            json.encodeToString(jsonObject).let { jsonString ->
                json.decodeFromString(jsonString)
            }
        }

    protected inline fun <reified T> dataSnapshotToList(dataSnapshot: DataSnapshot): List<T> =
        dataSnapshot.children.mapNotNull { child ->
            (child.value as? Map<*, *>)?.toJsonObject().let { jsonObject ->
                json.encodeToString(jsonObject).let { jsonString ->
                    json.decodeFromString(jsonString)
                }
            }
        }

    protected inline fun <reified T> dataSnapshotToMap(dataSnapshot: DataSnapshot): Map<String, T> =
        dataSnapshot.children.associateBy(
            { it.key.orEmpty() },
            {
                val jsonObject = (it.value as? Map<*, *>)?.toJsonObject()
                val jsonString = json.encodeToString(jsonObject)
                json.decodeFromString<T>(jsonString)
            }
        )
            .filterValues { it != null }
            .mapValues { entry -> entry.value }

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

    protected suspend inline fun <reified T> setValueToDatabase(ref: DatabaseReference, value: T?) =
        json.encodeToString(value).let { jsonString ->
            val map = gson.fromJson(jsonString, Map::class.java)
            suspendCoroutine { continuation: Continuation<Unit> ->
                ref.setValue(map)
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
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

    protected fun Map<*, *>.toJsonObject(): JsonObject {
        val map = mutableMapOf<String, JsonElement>()
        this.forEach {
            if (it.key is String) {
                map[it.key as String] = it.value.toJsonElement()
            }
        }
        return JsonObject(map)
    }

    private fun Any?.toJsonElement(): JsonElement {
        return when (this) {
            is Number -> JsonPrimitive(this)
            is Boolean -> JsonPrimitive(this)
            is String -> JsonPrimitive(this)
            is Array<*> -> this.toJsonArray()
            is List<*> -> this.toJsonArray()
            is Map<*, *> -> this.toJsonObject()
            is JsonElement -> this
            else -> JsonNull
        }
    }

    private fun Array<*>.toJsonArray(): JsonArray {
        val array = mutableListOf<JsonElement>()
        this.forEach { array.add(it.toJsonElement()) }
        return JsonArray(array)
    }

    private fun List<*>.toJsonArray(): JsonArray {
        val array = mutableListOf<JsonElement>()
        this.forEach { array.add(it.toJsonElement()) }
        return JsonArray(array)
    }
}
