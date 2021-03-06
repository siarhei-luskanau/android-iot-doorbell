package siarhei.luskanau.iot.doorbell.ui.imagedetails

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import siarhei.luskanau.iot.doorbell.common.AppNavigation

class ImageDetailsPresenterImpl(
    private val appNavigation: AppNavigation,
    private val fragment: Fragment,
    private val doorbellId: String,
    private val imageId: String
) : ImageDetailsPresenter {

    override fun getImageDetailsStateData(): LiveData<ImageDetailsState> =
        MutableLiveData<ImageDetailsState>().also { liveData ->
            liveData.postValue(
                runCatching {
                    NormalImageDetailsState(
                        ImagesFragmentViewPagerAdapter(
                            appNavigation = appNavigation,
                            fragment = fragment,
                            doorbellId = doorbellId,
                            imageId = imageId
                        )
                    )
                }.onFailure {
                    ErrorImageDetailsState(it)
                }.getOrNull()
            )
        }
}
