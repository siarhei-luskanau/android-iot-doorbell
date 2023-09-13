package siarhei.luskanau.iot.doorbell.ui.imagedetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository

class ImageDetailsPresenterImpl(
    private val doorbellId: String,
    private val imageId: String,
    private val doorbellRepository: DoorbellRepository,
) : ImageDetailsPresenter {

    override fun getImageDetailsStateFlow(): Flow<ImageDetailsState> = flowOf(Any())
        .map {
            val imageData = doorbellRepository.getImage(
                doorbellId = doorbellId,
                imageId = imageId,
            )
            NormalImageDetailsState(requireNotNull(imageData))
        }.catch {
            ErrorImageDetailsState(it)
        }
}
