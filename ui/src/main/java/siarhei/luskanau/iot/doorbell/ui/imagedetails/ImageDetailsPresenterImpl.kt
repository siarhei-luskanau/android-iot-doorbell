package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

class ImageDetailsPresenterImpl(
    private val imageData: ImageData?
) : ImageDetailsPresenter {

    override fun getImageDetailsStateData(): LiveData<ImageDetailsState> =
        MutableLiveData<ImageDetailsState>().also { liveData ->
            liveData.postValue(
                runCatching {
                    NormalImageDetailsState(requireNotNull(imageData))
                }.onFailure {
                    ErrorImageDetailsState(it)
                }.getOrNull()
            )
        }
}
