package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.datasource.images.ImagesDataSourceFactory
import timber.log.Timber
import javax.inject.Inject

class ImagesViewModel @Inject constructor(
        imagesDataSourceFactory: ImagesDataSourceFactory
) : ViewModel() {

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