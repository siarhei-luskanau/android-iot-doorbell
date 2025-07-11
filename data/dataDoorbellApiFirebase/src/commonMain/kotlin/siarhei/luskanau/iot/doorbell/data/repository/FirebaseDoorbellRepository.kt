package siarhei.luskanau.iot.doorbell.data.repository

import dev.gitlive.firebase.database.Query
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.CameraInfoData
import siarhei.luskanau.iot.doorbell.data.model.CameraxInfoData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.SizeData

class FirebaseDoorbellRepository :
    BaseFirebaseRepository(),
    DoorbellRepository {

    override suspend fun getDoorbellsList(
        size: Int,
        startAt: String?,
        orderAsc: Boolean
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

        val map = query.valueEvents.map { dataSnapshot ->
            dataSnapshot.children.associate {
                Pair(it.key.orEmpty(), it.value<DoorbellDto>())
            }
        }.firstOrNull().orEmpty()

        return map.values.toList()
            .filter { it.doorbellId != startAt }
            .map {
                DoorbellData(
                    doorbellId = it.doorbellId,
                    name = it.name,
                    info = it.info
                )
            }
    }

    override suspend fun getDoorbell(doorbellId: String): DoorbellData? =
        getAppDatabase().child(DOORBELLS_KEY).child(doorbellId).valueEvents
            .map { it.value<DoorbellDto>() }
            .map {
                DoorbellData(
                    doorbellId = it.doorbellId,
                    name = it.name,
                    info = it.info?.mapValues { entry -> entry.value }
                )
            }.firstOrNull()

    override suspend fun getCamerasList(doorbellId: String): List<CameraData> =
        getAppDatabase().child(CAMERAS_KEY).child(doorbellId).valueEvents
            .map { list -> list.children.toList().map { it.value<CameraDto>() } }
            .firstOrNull().orEmpty()
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
                    info = it.info?.let { info ->
                        CameraInfoData(
                            lensFacing = info.lensFacing,
                            infoSupportedHardwareLevel = info.infoSupportedHardwareLevel,
                            scalerStreamConfigurationMap = info.scalerStreamConfigurationMap,
                            controlAvailableEffects = info.controlAvailableEffects,
                            error = info.error
                        )
                    },
                    cameraxInfo = it.cameraxInfo?.let { cameraxInfo ->
                        CameraxInfoData(
                            error = cameraxInfo.error
                        )
                    }
                )
            }

    override suspend fun sendDoorbellData(doorbellData: DoorbellData) =
        getAppDatabase().child(DOORBELLS_KEY).child(doorbellData.doorbellId).setValue(
            DoorbellDto(
                doorbellId = doorbellData.doorbellId,
                name = doorbellData.name,
                info = doorbellData.info
            )
        )

    override suspend fun sendCamerasList(doorbellId: String, list: List<CameraData>) {
        val value = list.map { data ->
            CameraDto(
                cameraId = data.cameraId,
                name = data.name,
                sizes = data.sizes?.mapValues { entry ->
                    SizeDto(
                        width = entry.value.width,
                        height = entry.value.height
                    )
                },
                info = data.info?.let { info ->
                    CameraInfoDto(
                        lensFacing = info.lensFacing,
                        infoSupportedHardwareLevel = info.infoSupportedHardwareLevel,
                        scalerStreamConfigurationMap = info.scalerStreamConfigurationMap,
                        controlAvailableEffects = info.controlAvailableEffects,
                        error = info.error
                    )
                },
                cameraxInfo = data.cameraxInfo?.let { cameraxInfo ->
                    CameraxInfoDto(
                        error = cameraxInfo.error
                    )
                }
            )
        }.associateBy(
            { data -> data.cameraId },
            { data -> data }
        )
        getAppDatabase().child(CAMERAS_KEY).child(doorbellId).setValue(value)
    }

    override suspend fun sendCameraImageRequest(
        doorbellId: String,
        cameraId: String,
        isRequested: Boolean
    ) = getAppDatabase().child(IMAGE_REQUEST_KEY).child(doorbellId).child(cameraId)
        .setValue(isRequested)

    override suspend fun getCameraImageRequest(doorbellId: String): Map<String, Boolean> =
        getAppDatabase().child(IMAGE_REQUEST_KEY).child(doorbellId).valueEvents
            .map { dataSnapshot ->
                dataSnapshot.children.associate {
                    Pair(it.key.orEmpty(), it.value<Boolean>())
                }
            }.firstOrNull().orEmpty()

    override suspend fun getImagesList(
        doorbellId: String,
        size: Int,
        imageIdAt: String?,
        orderAsc: Boolean
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

        val map = query.valueEvents.map { dataSnapshot ->
            dataSnapshot.children.associate {
                Pair(it.key.orEmpty(), it.value<ImageDto>())
            }
        }.firstOrNull().orEmpty()

        return map.values.toList()
            .reversed()
            .filter { imageDto: ImageDto ->
                imageDto.imageId != imageIdAt
            }
            .map { imageDto ->
                ImageData(
                    imageId = imageDto.imageId,
                    imageUri = imageDto.imageStoragePath,
                    timestampString = Clock.System.now().toString()
                )
            }
    }

    override suspend fun getImage(doorbellId: String, imageId: String): ImageData? =
        getAppDatabase().child(IMAGES_KEY).child(doorbellId).child(imageId).valueEvents
            .map { it.value<ImageDto>() }
            .firstOrNull()
            ?.let { imageDto ->
                ImageData(
                    imageId = imageDto.imageId,
                    imageUri = imageDto.imageStoragePath,
                    timestampString = Clock.System.now().toString()
                )
            }

    companion object {
        private const val DOORBELLS_KEY = "doorbells"
        private const val CAMERAS_KEY = "cameras"
        internal const val IMAGES_KEY = "images"
        private const val IMAGE_REQUEST_KEY = "image_request"
    }
}
