package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.Query
import com.google.firebase.database.ServerValue
import java.text.DateFormat
import java.util.Calendar
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import siarhei.luskanau.iot.doorbell.data.model.SizeData

class FirebaseDoorbellRepository(
    private val imageRepository: ImageRepository
) : BaseFirebaseRepository(), DoorbellRepository {

    companion object {
        private const val DOORBELLS_KEY = "doorbells"
        private const val CAMERAS_KEY = "cameras"
        private const val IMAGES_KEY = "images"
        private const val IMAGE_REQUEST_KEY = "image_request"

        private val formatter = DateFormat.getDateTimeInstance()
    }

    override suspend fun getDoorbellsList(
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
            getValueFromDatabase(query),
            DoorbellDto::class.java
        )

        return map.values.toList()
            .filter { it.doorbellId != startAt }
            .map {
                DoorbellData(
                    doorbellId = it.doorbellId,
                    name = it.name,
                    isAndroidThings = it.isAndroidThings,
                    info = it.info?.mapValues { entry -> entry.value.toString() }
                )
            }
    }

    override suspend fun getDoorbell(deviceId: String): DoorbellData? =
        dataSnapshotObject(
            getValueFromDatabase(getAppDatabase().child(DOORBELLS_KEY).child(deviceId)),
            DoorbellDto::class.java
        )?.let {
            DoorbellData(
                doorbellId = it.doorbellId,
                name = it.name,
                isAndroidThings = it.isAndroidThings,
                info = it.info?.mapValues { entry -> entry.value.toString() }
            )
        }

    override suspend fun getCamerasList(deviceId: String): List<CameraData> =
        dataSnapshotToList(
            getValueFromDatabase(getAppDatabase().child(CAMERAS_KEY).child(deviceId)),
            CameraDto::class.java
        )
            .map {
                CameraData(
                    cameraId = it.cameraId,
                    name = it.name,
                    sizes = it.sizes?.mapValues { entry ->
                        SizeData(
                            entry.value.width,
                            entry.value.height
                        )
                    },
                    info = it.info?.mapValues { entry -> entry.value.toString() },
                    cameraxInfo = it.cameraxInfo?.mapValues { entry -> entry.value.toString() }
                )
            }

    override suspend fun sendDoorbellData(doorbellData: DoorbellData) =
        setValueToDatabase(
            getAppDatabase().child(DOORBELLS_KEY).child(doorbellData.doorbellId),
            serializeByMoshi(
                DoorbellDto(
                    doorbellId = doorbellData.doorbellId,
                    name = doorbellData.name,
                    isAndroidThings = doorbellData.isAndroidThings,
                    info = doorbellData.info
                )
            )
        )

    override suspend fun sendCamerasList(deviceId: String, list: List<CameraData>) =
        setValueToDatabase(
            getAppDatabase().child(CAMERAS_KEY).child(deviceId),
            serializeByMoshi(list.map {
                CameraDto(
                    cameraId = it.cameraId,
                    name = it.name,
                    sizes = it.sizes?.mapValues { entry ->
                        SizeDto(
                            width = entry.value.width,
                            height = entry.value.height
                        )
                    },
                    info = it.info,
                    cameraxInfo = it.cameraxInfo
                )
            })
        )

    override suspend fun sendCameraImageRequest(
        deviceId: String,
        cameraId: String,
        isRequested: Boolean
    ) =
        setValueToDatabase(
            getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId).child(cameraId),
            isRequested
        )

    override suspend fun getCameraImageRequest(deviceId: String): Map<String, Boolean> =
        dataSnapshotToMap(
            getValueFromDatabase(getAppDatabase().child(IMAGE_REQUEST_KEY).child(deviceId)),
            Boolean::class.java
        )

    override suspend fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile) {
        val imageId: String =
            getAppDatabase().child(IMAGES_KEY).child(deviceId).push().key.orEmpty()

        val uri: Uri? = putStreamToStorage(
            getAppStorage().child(imageId),
            imageRepository.openInputStream(imageFile)
        )

        setValueToDatabase(
            getAppDatabase().child(IMAGES_KEY).child(deviceId).child(imageId),
            serializeByMoshi(
                ImageDto(
                    imageId = imageId,
                    imageStoragePath = uri.toString(),
                    deviceId = deviceId,
                    cameraId = cameraId,
                    timestamp = 0
                )
            )
        )

        setValueToDatabase(
            getAppDatabase().child(IMAGES_KEY).child(deviceId).child(imageId).child("timestamp"),
            ServerValue.TIMESTAMP
        )
    }

    override suspend fun getImagesList(
        deviceId: String,
        size: Int,
        imageIdAt: String?,
        orderAsc: Boolean
    ): List<ImageData> {
        var query: Query = getAppDatabase().child(IMAGES_KEY).child(deviceId)

        query = query.orderByChild("image_id")

        query = if (orderAsc) {
            query.limitToLast(size)
        } else {
            query.limitToFirst(size)
        }

        imageIdAt?.let {
            query = if (orderAsc) {
                query.endAt(it, imageIdAt)
            } else {
                query.startAt(it, imageIdAt)
            }
        }

        val map = dataSnapshotToMap(
            getValueFromDatabase(query),
            ImageDto::class.java
        )

        return map.values.toList()
            .reversed()
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
