package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import com.google.firebase.database.Query
import com.google.firebase.database.ServerValue
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.CameraInfoData
import siarhei.luskanau.iot.doorbell.data.model.CameraxInfoData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.SizeData
import java.io.InputStream
import java.text.DateFormat
import java.util.Calendar

class FirebaseDoorbellRepository : BaseFirebaseRepository(), DoorbellRepository {

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
        orderAsc: Boolean,
    ): List<DoorbellData> {
        var query: Query = getAppDatabase().child(DOORBELLS_KEY)
        query = query.orderByChild("doorbell_id")
        query = if (orderAsc) {
            query.limitToFirst(size)
        } else {
            query.limitToLast(size)
        }

        startAt?.let {
            query = query.startAt(it, startAt)
        }

        val map = dataSnapshotToMap<DoorbellDto>(
            getValueFromDatabase(query),
        )

        return map.values.toList()
            .filter { it.doorbellId != startAt }
            .map {
                DoorbellData(
                    doorbellId = it.doorbellId,
                    name = it.name,
                    info = it.info,
                )
            }
    }

    override suspend fun getDoorbell(doorbellId: String): DoorbellData? =
        dataSnapshotObject<DoorbellDto>(
            getValueFromDatabase(getAppDatabase().child(DOORBELLS_KEY).child(doorbellId)),
        )?.let {
            DoorbellData(
                doorbellId = it.doorbellId,
                name = it.name,
                info = it.info?.mapValues { entry -> entry.value },
            )
        }

    override suspend fun getCamerasList(doorbellId: String): List<CameraData> =
        dataSnapshotToList<CameraDto>(
            getValueFromDatabase(getAppDatabase().child(CAMERAS_KEY).child(doorbellId)),
        )
            .map {
                CameraData(
                    cameraId = it.cameraId,
                    name = it.name,
                    sizes = it.sizes?.mapValues { entry ->
                        SizeData(
                            entry.value.width,
                            entry.value.height,
                        )
                    },
                    info = it.info?.let { info ->
                        CameraInfoData(
                            lensFacing = info.lensFacing,
                            infoSupportedHardwareLevel = info.infoSupportedHardwareLevel,
                            scalerStreamConfigurationMap = info.scalerStreamConfigurationMap,
                            controlAvailableEffects = info.controlAvailableEffects,
                            error = info.error,
                        )
                    },
                    cameraxInfo = it.cameraxInfo?.let { cameraxInfo ->
                        CameraxInfoData(
                            error = cameraxInfo.error,
                        )
                    },
                )
            }

    override suspend fun sendDoorbellData(doorbellData: DoorbellData) =
        setValueToDatabase(
            getAppDatabase().child(DOORBELLS_KEY).child(doorbellData.doorbellId),
            DoorbellDto(
                doorbellId = doorbellData.doorbellId,
                name = doorbellData.name,
                info = doorbellData.info,
            ),
        )

    override suspend fun sendCamerasList(doorbellId: String, list: List<CameraData>) =
        setValueToDatabase(
            getAppDatabase().child(CAMERAS_KEY).child(doorbellId),
            list.map { data ->
                CameraDto(
                    cameraId = data.cameraId,
                    name = data.name,
                    sizes = data.sizes?.mapValues { entry ->
                        SizeDto(
                            width = entry.value.width,
                            height = entry.value.height,
                        )
                    },
                    info = data.info?.let { info ->
                        CameraInfoDto(
                            lensFacing = info.lensFacing,
                            infoSupportedHardwareLevel = info.infoSupportedHardwareLevel,
                            scalerStreamConfigurationMap = info.scalerStreamConfigurationMap,
                            controlAvailableEffects = info.controlAvailableEffects,
                            error = info.error,
                        )
                    },
                    cameraxInfo = data.cameraxInfo?.let { cameraxInfo ->
                        CameraxInfoDto(
                            error = cameraxInfo.error,
                        )
                    },
                )
            }
                .associateBy(
                    { data -> data.cameraId },
                    { data -> data },
                ),
        )

    override suspend fun sendCameraImageRequest(
        doorbellId: String,
        cameraId: String,
        isRequested: Boolean,
    ) =
        setValueToDatabase(
            getAppDatabase().child(IMAGE_REQUEST_KEY).child(doorbellId).child(cameraId),
            isRequested,
        )

    override suspend fun getCameraImageRequest(doorbellId: String): Map<String, Boolean> =
        dataSnapshotToMap(
            getValueFromDatabase(getAppDatabase().child(IMAGE_REQUEST_KEY).child(doorbellId)),
        )

    override suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        imageInputStream: InputStream,
    ) {
        val imageId: String =
            getAppDatabase().child(IMAGES_KEY).child(doorbellId).push().key.orEmpty()

        val uri: Uri? = putStreamToStorage(
            getAppStorage().child(imageId),
            imageInputStream,
        )

        setValueToDatabase(
            getAppDatabase().child(IMAGES_KEY).child(doorbellId).child(imageId),
            ImageDto(
                imageId = imageId,
                imageStoragePath = uri.toString(),
                doorbellId = doorbellId,
                cameraId = cameraId,
                timestamp = 0,
            ),
        )

        setValueToDatabase(
            getAppDatabase().child(IMAGES_KEY).child(doorbellId).child(imageId).child("timestamp"),
            ServerValue.TIMESTAMP,
        )
    }

    override suspend fun getImagesList(
        doorbellId: String,
        size: Int,
        imageIdAt: String?,
        orderAsc: Boolean,
    ): List<ImageData> {
        var query: Query = getAppDatabase().child(IMAGES_KEY).child(doorbellId)

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

        val map = dataSnapshotToMap<ImageDto>(
            getValueFromDatabase(query),
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
                    timestampString = formatter.format(calendar.time),
                )
            }
    }

    override suspend fun getImage(
        doorbellId: String,
        imageId: String,
    ): ImageData? =
        dataSnapshotObject<ImageDto>(
            getValueFromDatabase(
                query = getAppDatabase().child(IMAGES_KEY).child(doorbellId).child(imageId),
            ),
        )?.let { imageDto ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = imageDto.timestamp
            ImageData(
                imageId = imageDto.imageId,
                imageUri = imageDto.imageStoragePath,
                timestampString = formatter.format(calendar.time),
            )
        }
}
