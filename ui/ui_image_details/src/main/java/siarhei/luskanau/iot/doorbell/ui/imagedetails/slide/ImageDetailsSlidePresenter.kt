package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import androidx.lifecycle.LiveData

interface ImageDetailsSlidePresenter {
    fun getImageDetailsSlideStateData(): LiveData<ImageDetailsSlideState>
}
