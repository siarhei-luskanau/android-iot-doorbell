package siarhei.luskanau.iot.doorbell.common

import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class ImagesDataSourceImpl(
    private val doorbellRepository: DoorbellRepository,
    private val deviceId: String
) : ImagesDataSource() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, ImageData> {
        val data = doorbellRepository.getImagesList(
            deviceId = deviceId,
            size = params.loadSize,
            imageIdAt = params.key,
            orderAsc = true
        )
        return LoadResult.Page(
            data = data,
            prevKey = params.key,
            nextKey = data.lastOrNull()?.imageId
        )
    }
}
