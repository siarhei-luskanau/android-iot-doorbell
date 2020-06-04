package siarhei.luskanau.iot.doorbell.common

import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class DefaultDoorbellsDataSource(
    private val doorbellRepository: DoorbellRepository
) : DoorbellsDataSource() {

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<DoorbellData>
    ) =
        getDoorbellsList(callback, params.requestedLoadSize, params.requestedInitialKey)

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<DoorbellData>) =
        getDoorbellsList(callback, params.requestedLoadSize, params.key)

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<DoorbellData>) = Unit

    override fun getKey(item: DoorbellData) = item.doorbellId

    private fun getDoorbellsList(
        callback: LoadCallback<DoorbellData>,
        size: Int,
        startAt: String? = null
    ) {
        runBlocking {
            callback.onResult(doorbellRepository.getDoorbellsList(size, startAt))
        }
    }
}
