package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.lifecycle.LiveData

interface ImageDetailsPresenter {
    fun getImageDetailsStateData(): LiveData<ImageDetailsState>
}