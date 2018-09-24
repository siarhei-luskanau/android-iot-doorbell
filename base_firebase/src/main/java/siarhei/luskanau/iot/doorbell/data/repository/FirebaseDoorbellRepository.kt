package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.Query
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.experimental.runBlocking
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import java.text.DateFormat
import java.util.*

class FirebaseDoorbellRepository(
        val imageRepository: ImageRepository
) : BaseFirebaseRepository(), DoorbellRepository {

    companion object {
        private const val DOORBELLS_KEY = "doorbells"
        private const val CAMERAS_KEY = "cameras"
        private const val IMAGES_KEY = "images"
        private const val IMAGE_REQUEST_KEY = "image_request"

        private val formatter = DateFormat.getDateTimeInstance()
    }

    override fun getDoorbellsList(
            size: Int,
            startAt: String?,
            orderAsc: Boolean
    ): List<DoorbellData> {
        var query: Query = getAppDatabase().child(DOORBELLS_KEY)
        query = query.orderByChild("doorbell_id")
        query = if (orderAsc)
            query.limitToFirst(size)
        else
            query.limitToLast(size)

        startAt?.let {
            query = query.startAt(it, startAt)
        }

        val map = dataSnapshotToMap(
                runBlocking { getValueFromDatabase(query) },
                DoorbellDto::class.java
        )

        return map.values.toList()
                .filter { it.doorbellId != startAt }
                .map {
                    DoorbellData(
                            doorbellId = it.doorbellId,
                            name = it.name,
                            isAndroidThings = it.isAndroidThings,
                            info = it.info

                    )
                }
    }

    override fun getCamerasList(deviceId: String): List<CameraData> =
            dataSnapshotToList(
                    runBlocking {
                        getValueFromDatabase(getAppDatabase().child(CAMERAS_KEY).child(deviceId))
                    },
                    CameraDto::class.java
            )
                    .map {
                        CameraData(
                                cameraId = it.cameraId,
                                name = it.name,
                                sizes = it.sizes,
                                info = it.info
                        )
                    }

    override fun sendDoorbellData(doorbellData: DoorbellData) = runBlocking {
        setValueToDatabase(
                getAppDatabase().child(DOORBELLS_KEY).child(doorbellData.doorbellId),
                serializeByGson(DoorbellDto(
                        doorbellId = doorbellData.doorbellId,
                        name = doorbellData.name,
                        isAndroidThings = doorbellData.isAndroidThings,
                        info = doorbellData.info

                ))
        )
    }

    override fun sendCamerasList(deviceId: String, list: List<CameraData>) = runBlocking {
        setValueToDatabase(
                getAppDatabase().child(CAMERAS_KEY).child(deviceId),
                serializeByGson(list.map {
                    CameraDto(
                            cameraId = it.cameraId,
                            name = it.name,
                            sizes = it.sizes,
                            info = it.info
                    )
                })
        )
    }

    override fun sendCameraImageRequest(
            deviceId: String,
            cameraId: String,
            isRequested: Boolean
    ) = runBlocking {
        setValueToDatabase(
                getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId).child(cameraId),
                isRequested
        )
    }

    override fun getCameraImageRequest(deviceId: String): Map<String, Boolean> =
            dataSnapshotToMap(
                    runBlocking {
                        getValueFromDatabase(getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId))
                    },
                    Boolean::class.java
            )

    override fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile) {
        val imageId: String = getAppDatabase().child(IMAGES_KEY).child(deviceId).push().key.orEmpty()

        val uri: Uri? = runBlocking {
            putStreamToStorage(getAppStorage().child(imageId), imageRepository.openInputStream(imageFile))
        }

        runBlocking {
            setValueToDatabase(
                    getAppDatabase().child(IMAGES_KEY).child(deviceId).child(imageId),
                    serializeByGson(ImageDto(
                            imageId = imageId,
                            imageStoragePath = uri.toString(),
                            deviceId = deviceId,
                            cameraId = cameraId,
                            timestamp = 0
                    ))
            )
        }

        runBlocking {
            setValueToDatabase(
                    getAppDatabase().child(IMAGES_KEY).child(deviceId).child(imageId).child("timestamp"),
                    ServerValue.TIMESTAMP
            )
        }
    }

    override fun getImagesList(
            deviceId: String,
            size: Int,
            imageIdAt: String?,
            orderAsc: Boolean
    ): List<ImageData> {
        var query: Query = getAppDatabase().child(IMAGES_KEY).child(deviceId)

        query = query.orderByChild("image_id")

        query = if (orderAsc)
            query.limitToFirst(size)
        else
            query.limitToLast(size)

        imageIdAt?.let {
            query = if (orderAsc) {
                query.startAt(it, imageIdAt)
            } else {
                query.endAt(it, imageIdAt)
            }
        }

        val map = dataSnapshotToMap(
                runBlocking { getValueFromDatabase(query) },
                ImageDto::class.java
        )

        return map.values.toList()
                .filter { imageDto: ImageDto ->
                    imageDto.imageId != imageIdAt
                }
                .map { imageDto ->
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = imageDto.timestamp
                    ImageData(
                            imageId = imageDto.imageId,
                            imageUri = imageDto.imageStoragePath,
                            timestampString = formatter.format(calendar.time)
                    )
                }
    }

}