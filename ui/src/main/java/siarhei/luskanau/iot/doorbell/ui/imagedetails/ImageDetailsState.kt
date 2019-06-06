package siarhei.luskanau.iot.doorbell.ui.imagedetails

import siarhei.luskanau.iot.doorbell.data.model.ImageData

sealed class ImageDetailsState

data class NormalImageDetailsState(val imageData: ImageData) : ImageDetailsState()

data class ErrorImageDetailsState(val error: Throwable) : ImageDetailsState()
