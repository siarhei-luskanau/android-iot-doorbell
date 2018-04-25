package siarhei.luskanau.iot.doorbell.data.repository

import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import durdinapps.rxfirebase2.RxFirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseStorage
import io.reactivex.Completable
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import timber.log.Timber

class FirebaseDoorbellRepository(
        override val gson: Gson
) : BaseFirebaseRepository(gson), DoorbellRepository {

    companion object {
        private const val DOORBELLS_KEY = "doorbells"
        private const val CAMERAS_KEY = "cameras"
        private const val IMAGES_KEY = "images"
        private const val IMAGE_REQUEST_KEY = "image_request"
    }

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

    override fun sendImage(deviceId: String, cameraId: String, imageBytes: ByteArray): Completable {
        val log = getAppDatabase().child("logs").push()
        return RxFirebaseStorage.putBytes(getAppStorage().child(log.key), imageBytes)
                .flatMapCompletable { taskSnapshot: UploadTask.TaskSnapshot ->
                    // RxFirebaseDatabase.setValue(log, ServerValue.TIMESTAMP)
                    // .andThen(RxFirebaseDatabase.setValue(log, taskSnapshot.downloadUrl))
                    Completable.complete()
                }
                .doOnError {
                    Timber.e(it)
                }
    }

}