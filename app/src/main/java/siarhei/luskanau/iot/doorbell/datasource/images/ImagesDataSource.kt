package siarhei.luskanau.iot.doorbell.datasource.images

import android.arch.paging.ItemKeyedDataSource
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository

class ImagesDataSource(
        private val cachedRepository: CachedRepository,
        private val deviceId: String
) : ItemKeyedDataSource<String, ImageData>() {

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<ImageData>): Unit =
            cachedRepository.loadAfterImages(
                    deviceId,
                    params.requestedLoadSize,
                    params.requestedInitialKey,
                    { callback.onResult(it) },
                    { invalidate() }
            )

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<ImageData>) =
            cachedRepository.loadAfterImages(
                    deviceId,
                    params.requestedLoadSize,
                    params.key,
                    { callback.onResult(it) },
                    { invalidate() }
            )

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<ImageData>) =
            cachedRepository.loadAfterBeforeImages(
                    deviceId,
                    params.requestedLoadSize,
                    params.key,
                    { callback.onResult(it) },
                    { invalidate() }
            )

    override fun getKey(item: ImageData): String = item.imageId

}