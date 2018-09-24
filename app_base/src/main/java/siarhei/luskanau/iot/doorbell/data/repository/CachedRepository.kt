package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface CachedRepository {

    fun loadAfterImages(
            deviceId: String,
            limit: Int,
            afterImageId: String?,
            onResult: (List<ImageData>) -> Unit,
            invalidate: () -> Unit
    )

    fun loadBeforeImages(
            deviceId: String,
            limit: Int,
            beforeImageId: String?,
            onResult: (List<ImageData>) -> Unit,
            invalidate: () -> Unit
    )

}