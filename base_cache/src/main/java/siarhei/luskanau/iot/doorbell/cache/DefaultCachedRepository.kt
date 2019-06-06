package siarhei.luskanau.iot.doorbell.cache

import kotlinx.coroutines.runBlocking
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.CachedRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository

class DefaultCachedRepository(
    private val doorbellRepository: DoorbellRepository,
    private val persistenceRepository: PersistenceRepository
) : CachedRepository {

    override fun loadAfterImages(
        deviceId: String,
        limit: Int,
        afterImageId: String?,
        onResult: (List<ImageData>) -> Unit,
        invalidate: () -> Unit
    ) =
        runBlocking {
            onResult.invoke(
                doorbellRepository.getImagesList(
                    deviceId = deviceId,
                    size = limit,
                    imageIdAt = afterImageId,
                    orderAsc = true
                )
            )
        }

    override fun loadBeforeImages(
        deviceId: String,
        limit: Int,
        beforeImageId: String?,
        onResult: (List<ImageData>) -> Unit,
        invalidate: () -> Unit
    ) =
        runBlocking {
            onResult.invoke(
                doorbellRepository.getImagesList(
                    deviceId = deviceId,
                    size = limit,
                    imageIdAt = beforeImageId,
                    orderAsc = false
                )
            )
        }

//    private fun getAfterImagesList(deviceId: String, callback: ItemKeyedDataSource.LoadCallback<ImageData>, size: Int, startAt: String? = null) {
//        Single.fromCallable { persistenceRepository.getImages(deviceId, startAt, size) }
//                .subscribeOn(schedulerSet.io)
//                .observeOn(schedulerSet.ui)
//                .subscribe(
//                        {
//
//                            if (it.size != size) {
//                                updateFromNetwork(deviceId, callback, size, startAt, it.size)
//                            } else {
//                                callback.onResult(it)
//                            }
//                        },
//                        { Timber.e(it) }
//                )
//    }
//
//    private fun updateFromNetwork(deviceId: String, callback: ItemKeyedDataSource.LoadCallback<ImageData>, size: Int, startAt: String? = null, persistenceLoadedSize: Int) =
//            doorbellRepository.listenImagesList(deviceId, size, startAt)
//                    .subscribeOn(schedulerSet.io)
//                    .observeOn(schedulerSet.ui)
//                    .subscribe(
//                            {
//                                persistenceRepository.insertImages(deviceId, it)
//                                if (it.size != persistenceLoadedSize) {
//                                    //invalidate()
//                                }
//                            },
//                            { Timber.e(it) }
//                    )
}