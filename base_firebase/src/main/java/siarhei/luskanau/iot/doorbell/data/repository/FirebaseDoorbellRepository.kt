package siarhei.luskanau.iot.doorbell.data.repository

import com.google.firebase.database.Query
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import durdinapps.rxfirebase2.RxFirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseStorage
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import timber.log.Timber
import java.util.*

class FirebaseDoorbellRepository(
        override val gson: Gson,
        val imageRepository: ImageRepository
) : BaseFirebaseRepository(gson), DoorbellRepository {

    companion object {
        private const val DOORBELLS_KEY = "doorbells"
        private const val CAMERAS_KEY = "cameras"
        private const val IMAGES_KEY = "images"
        private const val IMAGE_REQUEST_KEY = "image_request"
    }

    override fun listenDoorbellsList(
            size: Int?,
            startAt: String?
    ): Single<List<DoorbellData>> {
        var query: Query = getAppDatabase().child(DOORBELLS_KEY)
        query = query.orderByChild("doorbell_id")

        size?.let {
            query = query.limitToFirst(it)
        }

        startAt?.let {
            query = query.startAt(it, startAt)
        }

        return RxFirebaseDatabase
                .observeValueEvent(query)
                .map {
                    dataSnapshotToList(it, DoorbellData::class.java)
                            .filter { it.doorbellId != startAt }
                }
                .firstOrError()
    }

    override fun listenCamerasList(deviceId: String): Flowable<List<CameraData>> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(CAMERAS_KEY).child(deviceId)
            )
            .map { dataSnapshotToList(it, CameraData::class.java) }

    override fun listenImagesList(
            deviceId: String,
            size: Int?,
            startAt: String?
    ): Single<List<ImageData>> {
        var query: Query = getAppDatabase().child(IMAGES_KEY).child(deviceId)
        query = query.orderByChild("image_id")

        size?.let {
            query = query.limitToFirst(it)
        }

        startAt?.let {
            query = query.startAt(it, startAt)
        }

        return RxFirebaseDatabase
                .observeValueEvent(query)
                .map {
                    dataSnapshotToList(it, ImageData::class.java)
                        .filter { it.imageId != startAt }
                }
                .firstOrError()
    }

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
            .andThen(
                    RxFirebaseDatabase.setValue(
                            getAppDatabase().child(IMAGES_KEY).child(deviceId).push(),
                            serializeByGson(ImageData(Date().toString()))
                    ))

    override fun listenCameraImageRequest(deviceId: String): Flowable<Map<String, Boolean>> = RxFirebaseDatabase
            .observeValueEvent(
                    getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId)
            )
            .map { dataSnapshotToMap(it, Boolean::class.java) }

    override fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile): Completable {
        val log = getAppDatabase().child("logs").push()
        return RxFirebaseStorage.putStream(getAppStorage().child(log.key), imageRepository.openInputStream(imageFile))
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