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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.serializer
import java.io.InputStream
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Suppress("TooManyFunctions")
open class BaseFirebaseRepository {

    protected val json by lazy {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    companion object {
        protected const val DOORBELL_APP_KEY = "doorbell_app"
    }

    protected fun getAppDatabase(): DatabaseReference =
        Firebase.database.getReference(DOORBELL_APP_KEY)

    protected fun getAppStorage(): StorageReference =
        Firebase.storage.getReference(DOORBELL_APP_KEY)

    protected inline fun <reified T> dataSnapshotObject(dataSnapshot: DataSnapshot): T? =
        (dataSnapshot.value as? Map<*, *>)?.toJsonElement()?.let { jsonElement ->
            json.decodeFromJsonElement(
                deserializer = serializer(),
                element = jsonElement,
            )
        }

    protected inline fun <reified T> dataSnapshotToList(dataSnapshot: DataSnapshot): List<T> =
        dataSnapshot.children.mapNotNull { child ->
            (child.value as? Map<*, *>)?.toJsonElement()?.let { jsonElement ->
                json.decodeFromJsonElement(
                    deserializer = serializer(),
                    element = jsonElement,
                )
            }
        }

    protected inline fun <reified T> dataSnapshotToMap(dataSnapshot: DataSnapshot): Map<String, T> =
        dataSnapshot.children.associateBy(
            { it.key.orEmpty() },
            {
                val jsonElement = when (val value = it.value) {
                    is String -> JsonPrimitive(value)
                    is Map<*, *> -> value.toJsonElement()
                    else -> JsonPrimitive(value.toString())
                }
                json.decodeFromJsonElement(
                    deserializer = serializer<T>(),
                    element = jsonElement,
                )
            },
        )
            .filterValues { it != null }
            .mapValues { entry -> entry.value }

    protected suspend fun putStreamToStorage(
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

    protected suspend inline fun <reified T> setValueToDatabase(ref: DatabaseReference, value: T?) =
        json.encodeToJsonElement(serializer = serializer(), value = value).let { jsonElement ->
            suspendCoroutine { continuation: Continuation<Unit> ->
                ref.setValue(jsonElement.toMap())
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

    protected fun JsonElement.toMap(): Any? =
        when (this) {
            is JsonNull -> null
            is JsonArray -> this.map { it.toMap() }
            is JsonObject -> this.mapValues { it.value.toMap() }
            is JsonPrimitive -> this.content
        }

    protected fun Map<*, *>.toJsonElement(): JsonElement {
        val map: MutableMap<String, JsonElement> = mutableMapOf()
        this.forEach {
            val key = it.key as? String ?: return@forEach
            val value = it.value ?: return@forEach
            when (value) {
                is Map<*, *> -> map[key] = (value).toJsonElement()
                is List<*> -> map[key] = value.toJsonElement()
                else -> map[key] = JsonPrimitive(value.toString())
            }
        }
        return JsonObject(map)
    }

    private fun List<*>.toJsonElement(): JsonElement {
        val list: MutableList<JsonElement> = mutableListOf()
        this.forEach {
            val value = it ?: return@forEach
            when (value) {
                is Map<*, *> -> list.add((value).toJsonElement())
                is List<*> -> list.add(value.toJsonElement())
                else -> list.add(JsonPrimitive(value.toString()))
            }
        }
        return JsonArray(list)
    }
}
