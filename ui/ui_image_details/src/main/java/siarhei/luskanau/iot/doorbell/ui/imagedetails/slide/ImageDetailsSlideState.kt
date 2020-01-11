package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import siarhei.luskanau.iot.doorbell.data.model.ImageData

sealed class ImageDetailsSlideState

data class NormalImageDetailsSlideState(val imageData: ImageData) : ImageDetailsSlideState()

data class ErrorImageDetailsSlideState(val error: Throwable) : ImageDetailsSlideState()
