package siarhei.luskanau.iot.doorbell.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson

abstract class BaseFirebaseRepository(open val gson: Gson) {

    companion object {
        protected const val DOORBELL_APP_KEY = "doorbell_app"
    }

    init {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
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
            dataSnapshot.children.associateBy(
                    { it.key },
                    { gson.fromJson(gson.toJson(it.value), type) }
            )

}