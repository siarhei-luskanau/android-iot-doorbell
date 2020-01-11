package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import siarhei.luskanau.iot.doorbell.common.AppNavigationArgs
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

class ImageDetailsPresenterImpl(
    private val appNavigationArgs: AppNavigationArgs,
    private val fragment: Fragment,
    private val doorbellData: DoorbellData?,
    private val imageData: ImageData?
) : ImageDetailsPresenter {

    override fun getImageDetailsStateData(): LiveData<ImageDetailsState> =
        MutableLiveData<ImageDetailsState>().also { liveData ->
            liveData.postValue(
                runCatching {
                    NormalImageDetailsState(
                        ImagesFragmentViewPagerAdapter(
                            appNavigationArgs = appNavigationArgs,
                            fragment = fragment,
                            doorbellData = requireNotNull(doorbellData),
                            imageData = requireNotNull(imageData)
                        )
                    )
                }.onFailure {
                    ErrorImageDetailsState(it)
                }.getOrNull()
            )
        }
}
