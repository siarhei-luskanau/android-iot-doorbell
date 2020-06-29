package siarhei.luskanau.iot.doorbell.common

import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class DefaultDoorbellsDataSource(
    private val doorbellRepository: DoorbellRepository
) : DoorbellsDataSource() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, DoorbellData> {
        val data = doorbellRepository.getDoorbellsList(params.loadSize, params.key)
        return LoadResult.Page(
            data = data,
            prevKey = params.key,
            nextKey = data.lastOrNull()?.doorbellId
        )
    }
}
