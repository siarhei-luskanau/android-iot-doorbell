package siarhei.luskanau.iot.doorbell.ui.imagedetails.slide

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class ImageDetailsSlidePresenterImpl(
    private val doorbellId: String,
    private val imageId: String,
    private val doorbellRepository: DoorbellRepository
) : ImageDetailsSlidePresenter {

    override fun getImageDetailsSlideStateFlow(): Flow<ImageDetailsSlideState> = flow {
        runCatching {
            val imageData = doorbellRepository.getImage(
                doorbellId = doorbellId,
                imageId = imageId
            )
            NormalImageDetailsSlideState(requireNotNull(imageData))
        }.onFailure {
            ErrorImageDetailsSlideState(it)
        }.getOrThrow()
    }
}
