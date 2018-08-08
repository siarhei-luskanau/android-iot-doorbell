package siarhei.luskanau.iot.doorbell.cache

import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository

class StubCachedRepository : CachedRepository {

    override fun loadAfterImages(
            deviceId: String,
            limit: Int,
            afterImageId: String?,
            onResult: (List<ImageData>) -> Unit,
            invalidate: () -> Unit
    ) {
        val list = mutableListOf<ImageData>()

        val id: Long = try {
            afterImageId.orEmpty().toLong()
        } catch (t: Throwable) {
            0
        }
        for (i in 1..limit) {
            list.add(ImageData((id + i).toString()))
        }

        onResult(list)
    }

    override fun loadAfterBeforeImages(
            deviceId: String,
            limit: Int,
            afterImageId: String?,
            onResult: (List<ImageData>) -> Unit,
            invalidate: () -> Unit
    ) {
        val list = mutableListOf<ImageData>()

        val id: Long = try {
            afterImageId.orEmpty().toLong()
        } catch (t: Throwable) {
            0
        }
        for (i in 1..limit) {
            if ((id - i) > 0) {
                list.add(ImageData((id - i).toString()))
            }
        }

        onResult(list)
    }

}