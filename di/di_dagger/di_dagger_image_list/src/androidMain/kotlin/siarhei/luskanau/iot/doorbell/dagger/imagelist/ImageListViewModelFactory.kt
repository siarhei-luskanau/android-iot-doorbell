package siarhei.luskanau.iot.doorbell.dagger.imagelist

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

class ImageListViewModelFactory(
    private val commonComponent: CommonComponent,
    private val appNavigation: AppNavigation,
    private val args: Bundle?
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        ImageListViewModel::class.java.isAssignableFrom(modelClass) -> ImageListViewModel(
            doorbellId = "doorbellId",
            appNavigation = appNavigation,
            doorbellRepository = commonComponent.provideDoorbellRepository(),
            imagesDataSourceFactory = commonComponent.provideImagesDataSourceFactory()
        )

        else -> modelClass.getConstructor().newInstance()
    } as T
}
