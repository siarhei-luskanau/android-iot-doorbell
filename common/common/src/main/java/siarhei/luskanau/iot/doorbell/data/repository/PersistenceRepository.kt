package siarhei.luskanau.iot.doorbell.data.repository

import kotlinx.coroutines.flow.Flow
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface PersistenceRepository {

    fun getImages(doorbellId: String, afterImageId: String?, limit: Int): Flow<List<ImageData>>

    suspend fun insertImages(doorbellId: String, images: List<ImageData>)
}
