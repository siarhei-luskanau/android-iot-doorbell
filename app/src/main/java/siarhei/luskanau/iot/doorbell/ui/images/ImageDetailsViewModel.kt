package siarhei.luskanau.iot.doorbell.ui.images

import siarhei.luskanau.iot.doorbell.datasource.images.ImagesDataSourceFactory
import siarhei.luskanau.iot.doorbell.viewmodel.BaseViewModel
import javax.inject.Inject

class ImageDetailsViewModel @Inject constructor(
    imagesDataSourceFactory: ImagesDataSourceFactory
) : BaseViewModel()