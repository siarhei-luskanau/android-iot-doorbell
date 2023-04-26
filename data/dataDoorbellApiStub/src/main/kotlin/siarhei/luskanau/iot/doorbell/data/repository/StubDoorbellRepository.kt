package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.coroutines.delay
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import java.io.IOException
import java.io.InputStream
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

@Suppress("TooManyFunctions")
class StubDoorbellRepository : DoorbellRepository {

    override suspend fun getDoorbellsList(
        size: Int,
        startAt: String?,
        orderAsc: Boolean,
    ): List<DoorbellData> {
        delay()
        if (startAt != null && Random.nextInt(PERCENT_FULL) > PERCENT_ERROR) {
            throw IOException("test")
        }
        val fromToPair = getFromToRange(
            size = size,
            startAt = startAt?.toInt(),
            orderAsc = orderAsc,
            minCount = MIN_COUNT,
            maxCount = DOORBELL_MAX_COUNT,
        )
        val list = mutableListOf<DoorbellData>()
        fromToPair?.let {
            for (i in fromToPair.first..fromToPair.second) {
                list.add(
                    DoorbellData(
                        doorbellId = "$i",
                        name = "doorbell_$i",
                    ),
                )
            }
        }
        return list
    }

    override suspend fun getDoorbell(doorbellId: String): DoorbellData {
        delay()
        return DoorbellData(
            doorbellId = doorbellId,
            name = "doorbell_$doorbellId",
        )
    }

    override suspend fun getCamerasList(doorbellId: String): List<CameraData> {
        delay()
        return listOf(
            CameraData(
                cameraId = "0",
                name = "camera0",
            ),
            CameraData(
                cameraId = "1",
                name = "camera1",
            ),
            CameraData(
                cameraId = "2",
                name = "camera2",
            ),
        )
    }

    override suspend fun sendDoorbellData(doorbellData: DoorbellData) {
        delay()
    }

    override suspend fun sendCamerasList(doorbellId: String, list: List<CameraData>) {
        delay()
    }

    override suspend fun sendCameraImageRequest(
        doorbellId: String,
        cameraId: String,
        isRequested: Boolean,
    ) {
        delay()
    }

    override suspend fun getCameraImageRequest(doorbellId: String): Map<String, Boolean> {
        delay()
        return emptyMap()
    }

    override suspend fun sendImage(
        doorbellId: String,
        cameraId: String,
        imageInputStream: InputStream,
    ) {
        delay()
    }

    override suspend fun getImage(
        doorbellId: String,
        imageId: String,
    ): ImageData {
        delay()
        return ImageData(
            imageId = imageId,
            imageUri = imageUriList[imageId.toInt() % imageUriList.size],
        )
    }

    override suspend fun getImagesList(
        doorbellId: String,
        size: Int,
        imageIdAt: String?,
        orderAsc: Boolean,
    ): List<ImageData> {
        delay()
        if (imageIdAt != null && Random.nextInt(PERCENT_FULL) > PERCENT_ERROR) {
            throw IOException("test")
        }
        val fromToPair = getFromToRange(
            size = size,
            startAt = imageIdAt?.toInt(),
            orderAsc = orderAsc,
            minCount = MIN_COUNT,
            maxCount = IMAGE_MAX_COUNT,
        )
        val list = mutableListOf<ImageData>()
        fromToPair?.let {
            for (i in fromToPair.first..fromToPair.second) {
                list.add(
                    ImageData(
                        imageId = "$i",
                        imageUri = imageUriList[i % imageUriList.size],
                        timestampString = "timestampString_$i",
                    ),
                )
            }
        }
        return list
    }

    @Suppress("ComplexMethod", "ReturnCount")
    fun getFromToRange(
        size: Int,
        startAt: Int?,
        orderAsc: Boolean,
        minCount: Int,
        maxCount: Int,
    ): Pair<Int, Int>? {
        startAt?.let { startAtInt ->
            if (orderAsc && startAtInt >= maxCount - 1) {
                return null
            }
            if (orderAsc.not() && startAtInt <= minCount) {
                return null
            }
        }

        val from: Int = if (orderAsc) {
            startAt?.let { it + 1 } ?: 0
        } else {
            startAt?.let { it - 1 } ?: (size - 1)
        }

        val to: Int = if (orderAsc) {
            min(maxCount - 1, startAt?.let { it + size } ?: (size - 1))
        } else {
            max(0, startAt?.let { max((it - size), minCount) } ?: 0)
        }

        return Pair(from, to)
    }

    private suspend fun delay() {
        delay(Random.nextLong(from = DELAY_MIN, until = DELAY_MAX))
    }

    companion object {
        private const val DELAY_MIN = 1_000L
        private const val DELAY_MAX = 2_000L
        private const val MIN_COUNT = 0
        private const val DOORBELL_MAX_COUNT = 50
        private const val IMAGE_MAX_COUNT = 150
        private const val PERCENT_FULL = 100
        private const val PERCENT_ERROR = 90
        private val imageUriList = (1..10).map {
            "https://github.com/google-developer-training/" +
                "android-basics-kotlin-affirmations-app-solution/" +
                "raw/main/app/src/main/res/drawable/image$it.jpg"
        }
    }
}
