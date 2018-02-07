package siarhei.luskanau.iot.doorbell.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

class FirebaseDoorbellRepository(val gson: Gson) : DoorbellRepository {

    init {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    override fun ping(deviceId: String): Completable = Completable.complete()

    override fun listenDoorbellsList(): Flowable<List<DoorbellData>> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(DOORBELLS_KEY)
            )
            .map { dataSnapshotToList(it, DoorbellData::class.java) }

    override fun listenCamerasList(deviceId: String): Flowable<List<CameraData>> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(CAMERAS_KEY).child(deviceId)
            )
            .map { dataSnapshotToList(it, CameraData::class.java) }

    override fun listenImagesList(deviceId: String): Flowable<List<ImageData>> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(IMAGES_KEY).child(deviceId)
            )
            .map { dataSnapshotToList(it, ImageData::class.java) }

    override fun sendDoorbellData(doorbellData: DoorbellData): Completable = RxFirebaseDatabase
            .setValue(
                    getAppDatabase().child(DOORBELLS_KEY).child(doorbellData.doorbellId),
                    serializeByGson(doorbellData)
            )

    override fun sendCamerasList(deviceId: String, list: List<CameraData>): Completable = RxFirebaseDatabase
            .setValue(
                    getAppDatabase().child(CAMERAS_KEY).child(deviceId),
                    serializeByGson(list)
            )

    override fun sendIpAddressMap(deviceId: String, ipAddressMap: Map<String, String>): Completable = RxFirebaseDatabase
            .setValue(
                    getAppDatabase().child(IP_ADDRESS_KEY).child(deviceId),
                    serializeByGson(ipAddressMap)
            )

    override fun sendCameraImageRequest(
            deviceId: String,
            cameraId: String,
            isRequested: Boolean
    ): Completable = RxFirebaseDatabase
            .setValue(
                    getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId).child(cameraId),
                    isRequested
            )

    override fun listenCameraImageRequest(deviceId: String): Flowable<Map<String, Boolean>> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId)
            )
            .map { dataSnapshotToMap(it, Boolean::class.java) }

    private fun getAppDatabase(): DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DOORBELL_APP_KEY)

    private fun <T> dataSnapshotToList(dataSnapshot: DataSnapshot, type: Class<T>): List<T> =
            dataSnapshot.children.map { gson.fromJson(gson.toJson(it.value), type) }

    private fun <T> dataSnapshotToMap(dataSnapshot: DataSnapshot, type: Class<T>): Map<String, T> =
            dataSnapshot.children.associateBy(
                    { it.key },
                    { gson.fromJson(gson.toJson(it.value), type) }
            )

    private fun serializeByGson(src: Any?): Any? =
            gson.fromJson(gson.toJson(src), Object::class.java)

    companion object {
        private const val DOORBELL_APP_KEY = "doorbell_app"
        private const val DOORBELLS_KEY = "doorbells"
        private const val CAMERAS_KEY = "cameras"
        private const val IMAGES_KEY = "images"
        private const val IP_ADDRESS_KEY = "ip_address"
        private const val IMAGE_REQUEST_KEY = "image_request"
    }

}