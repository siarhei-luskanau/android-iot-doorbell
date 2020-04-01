package siarhei.luskanau.iot.doorbell.dagger.imagelist

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

class ImageListViewModelFactory constructor(
    private val commonComponent: CommonComponent,
    private val appNavigation: AppNavigation,
    private val args: Bundle?
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {

            ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
                doorbellData = args?.let { ImageListFragmentArgs.fromBundle(it).doorbellData },
                appNavigation = appNavigation,
                doorbellRepository = commonComponent.provideDoorbellRepository(),
                imagesDataSourceFactory = commonComponent.provideImagesDataSourceFactory(),
                uptimeRepository = commonComponent.provideUptimeRepository()
            )

            else -> modelClass.getConstructor().newInstance()
        } as T
}
