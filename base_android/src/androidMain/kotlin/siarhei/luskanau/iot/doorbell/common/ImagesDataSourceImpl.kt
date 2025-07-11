package siarhei.luskanau.iot.doorbell.common

import androidx.paging.PagingState
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class ImagesDataSourceImpl(
    private val doorbellRepository: DoorbellRepository,
    private val doorbellId: String
) : ImagesDataSource() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, ImageData> = try {
        val data = doorbellRepository.getImagesList(
            doorbellId = doorbellId,
            size = params.loadSize,
            imageIdAt = params.key,
            orderAsc = true
        )
        LoadResult.Page(
            data = data,
            prevKey = params.key,
            nextKey = data.lastOrNull()?.imageId
        )
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }

    override fun getRefreshKey(state: PagingState<String, ImageData>): String? = null
}
