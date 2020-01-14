package siarhei.luskanau.iot.doorbell.data.repository

import kotlin.math.max
import kotlin.math.min
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

private const val MIN_COUNT = 0
private const val DOORBELL_MAX_COUNT = 50
private const val IMAGE_MAX_COUNT = 150

class DoorbellRepositoryFake : DoorbellRepository {

    override suspend fun getDoorbellsList(
        size: Int,
        startAt: String?,
        orderAsc: Boolean
    ): List<DoorbellData> {
        val fromToPair = getFromToRange(
            size = size,
            startAt = startAt,
            orderAsc = orderAsc,
            minCount = MIN_COUNT,
            maxCount = DOORBELL_MAX_COUNT
        )
        val list = mutableListOf<DoorbellData>()
        fromToPair?.let {
            for (i in fromToPair.first..fromToPair.second) {
                list.add(
                    DoorbellData(
                        doorbellId = "$i",
                        name = "doorbell_$i",
                        isAndroidThings = i % 2 == 0
                    )
                )
            }
        }
        return list
    }

    override suspend fun getDoorbell(deviceId: String): DoorbellData =
        DoorbellData(
            doorbellId = deviceId,
            name = "doorbell_$deviceId",
            isAndroidThings = false
        )

    override suspend fun getCamerasList(deviceId: String): List<CameraData> =
        listOf(
            CameraData(
                cameraId = "0",
                name = "camera0"
            ),
            CameraData(
                cameraId = "1",
                name = "camera1"
            ),
            CameraData(
                cameraId = "2",
                name = "camera2"
            )
        )

    override suspend fun sendDoorbellData(doorbellData: DoorbellData) {}

    override suspend fun sendCamerasList(deviceId: String, list: List<CameraData>) {}

    override suspend fun sendCameraImageRequest(
        deviceId: String,
        cameraId: String,
        isRequested: Boolean
    ) {
    }

    override suspend fun getCameraImageRequest(deviceId: String): Map<String, Boolean> =
        emptyMap()

    override suspend fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile) {}

    override suspend fun getImagesList(
        deviceId: String,
        size: Int,
        imageIdAt: String?,
        orderAsc: Boolean
    ): List<ImageData> {
        val fromToPair = getFromToRange(
            size = size,
            startAt = imageIdAt,
            orderAsc = orderAsc,
            minCount = MIN_COUNT,
            maxCount = IMAGE_MAX_COUNT
        )
        val list = mutableListOf<ImageData>()
        fromToPair?.let {
            for (i in fromToPair.first..fromToPair.second) {
                list.add(
                    ImageData(
                        imageId = "$i",
                        imageUri = "imageUri_$i",
                        timestampString = "timestampString_$i"
                    )
                )
            }
        }
        return list
    }

    fun getFromToRange(
        size: Int,
        startAt: String?,
        orderAsc: Boolean,
        minCount: Int,
        maxCount: Int
    ): Pair<Int, Int>? {
        startAt?.toInt()?.let { startAtInt ->
            if (orderAsc && startAtInt >= maxCount - 1) {
                return null
            }
            if (orderAsc.not() && startAtInt <= minCount) {
                return null
            }
        }

        val from: Int = if (orderAsc) {
            startAt?.let { it.toInt() + 1 } ?: 0
        } else {
            startAt?.let { it.toInt() - 1 } ?: size - 1
        }

        val to: Int = if (orderAsc) {
            min(maxCount - 1, startAt?.let { it.toInt() + size } ?: size - 1)
        } else {
            max(0, startAt?.let { max((it.toInt() - size), minCount) } ?: 0)
        }

        return Pair(from, to)
    }
}
