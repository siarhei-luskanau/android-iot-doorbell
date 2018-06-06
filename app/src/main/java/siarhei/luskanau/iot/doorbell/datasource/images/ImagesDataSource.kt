package siarhei.luskanau.iot.doorbell.datasource.images

import android.arch.paging.ItemKeyedDataSource
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber

class ImagesDataSource(
        private val schedulerSet: SchedulerSet,
        private val doorbellRepository: DoorbellRepository,
        private val deviceId: String
) : ItemKeyedDataSource<String, ImageData>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<ImageData>) =
            getImagesList(callback, params.requestedLoadSize, params.requestedInitialKey)

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<ImageData>) =
            getImagesList(callback, params.requestedLoadSize, params.key)

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<ImageData>) {}

    override fun getKey(item: ImageData): String = item.imageId

    private fun getImagesList(callback: LoadCallback<ImageData>, size: Int? = null, startAt: String? = null) {
        doorbellRepository.listenImagesList(deviceId, size, startAt)
                .subscribeOn(schedulerSet.io)
                .observeOn(schedulerSet.ui)
                .subscribe(
                        { callback.onResult(it) },
                        { Timber.e(it) }
                )
    }

}