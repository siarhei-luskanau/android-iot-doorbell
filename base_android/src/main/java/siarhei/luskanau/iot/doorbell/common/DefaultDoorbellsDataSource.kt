package siarhei.luskanau.iot.doorbell.common

import androidx.paging.PagingState
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class DefaultDoorbellsDataSource(
    private val doorbellRepository: DoorbellRepository
) : DoorbellsDataSource() {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun load(params: LoadParams<String>): LoadResult<String, DoorbellData> =
        try {
            val data = doorbellRepository.getDoorbellsList(params.loadSize, params.key)
            LoadResult.Page(
                data = data,
                prevKey = params.key,
                nextKey = data.lastOrNull()?.doorbellId
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }

    override fun getRefreshKey(state: PagingState<String, DoorbellData>): String? = null
}
