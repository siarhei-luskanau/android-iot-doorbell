package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import kotlinx.coroutines.flow.Flow

interface ImageDetailsSlidePresenter {
    fun getImageDetailsSlideStateFlow(): Flow<ImageDetailsSlideState>
}
