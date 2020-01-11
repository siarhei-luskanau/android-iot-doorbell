package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

class ImageDetailsSlidePresenterImpl(
    private val imageData: ImageData?
) : ImageDetailsSlidePresenter {

    override fun getImageDetailsSlideStateData(): LiveData<ImageDetailsSlideState> =
        MutableLiveData<ImageDetailsSlideState>().also { liveData ->
            liveData.postValue(
                runCatching {
                    NormalImageDetailsSlideState(requireNotNull(imageData))
                }.onFailure {
                    ErrorImageDetailsSlideState(it)
                }.getOrNull()
            )
        }
}
