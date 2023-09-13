package siarhei.luskanau.iot.doorbell.ui.imagedetails

import kotlinx.coroutines.flow.Flow

interface ImageDetailsPresenter {
    fun getImageDetailsStateFlow(): Flow<ImageDetailsState>
}
