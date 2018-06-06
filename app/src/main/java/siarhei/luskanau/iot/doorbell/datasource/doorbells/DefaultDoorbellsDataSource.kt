package siarhei.luskanau.iot.doorbell.datasource.doorbells

import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber

class DefaultDoorbellsDataSource(
        private val schedulerSet: SchedulerSet,
        private val doorbellRepository: DoorbellRepository
) : DoorbellsDataSource() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<DoorbellData>) =
            getDoorbellsList(callback, params.requestedLoadSize, params.requestedInitialKey)

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<DoorbellData>) =
            getDoorbellsList(callback, params.requestedLoadSize, params.key)

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<DoorbellData>) {}

    override fun getKey(item: DoorbellData) = item.doorbellId

    private fun getDoorbellsList(callback: LoadCallback<DoorbellData>, size: Int? = null, startAt: String? = null) {
        doorbellRepository.listenDoorbellsList(size, startAt)
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.ui)
                .subscribe(
                        { callback.onResult(it) },
                        { Timber.e(it) }
                )
    }

}