package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.datasource.images.ImagesDataSourceFactory
import timber.log.Timber
import javax.inject.Inject

class ImagesViewModel @Inject constructor(
    imagesDataSourceFactory: ImagesDataSourceFactory
) : BaseViewModel() {

    val deviceIdLiveData = MutableLiveData<String>()
    val imagesLiveData: LiveData<PagedList<ImageData>> =
            Transformations.switchMap(deviceIdLiveData) { deviceId: String ->
                LivePagedListBuilder(
                        imagesDataSourceFactory.createDataSourceFactory(deviceId),
                        PagedList.Config.Builder()
                                .setPageSize(2)
                                .setInitialLoadSizeHint(3)
                                .build()
                )
                        .setBoundaryCallback(object : PagedList.BoundaryCallback<ImageData>() {
                            override fun onZeroItemsLoaded() {
                                Timber.d("onZeroItemsLoaded")
                            }

                            override fun onItemAtFrontLoaded(itemAtFront: ImageData) {
                                Timber.d("onItemAtFrontLoaded: $itemAtFront")
                            }

                            override fun onItemAtEndLoaded(itemAtEnd: ImageData) {
                                Timber.d("onItemAtEndLoaded: $itemAtEnd")
                            }
                        })
                        .build()
            }
}