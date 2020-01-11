package siarhei.luskanau.iot.doorbell.dagger.imagelist

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

@Module
class ImageListBuilderModule {

    @Provides
    fun providesFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>
    ): FragmentFactory = DaggerFragmentFactory(
        providers
    )

    @Provides
    fun provideImageListViewModel(
        commonComponent: CommonComponent
    ) = ImageListViewModel(
        schedulerSet = commonComponent.provideSchedulerSet(),
        doorbellRepository = commonComponent.provideDoorbellRepository(),
        imagesDataSourceFactory = commonComponent.provideImagesDataSourceFactory(),
        uptimeRepository = commonComponent.provideUptimeRepository()
    )

    @Provides
    fun provideImageListFragment(
        viewModelFactory: ViewModelProvider.Factory,
        appNavigation: AppNavigation,
        commonComponent: CommonComponent
    ) = ImageListFragment { fragment: Fragment ->
        val doorbellData = commonComponent.provideAppNavigationArgs()
            .getImagesFragmentArgs(fragment.arguments)
        val viewModel = ViewModelProvider(fragment, viewModelFactory)
            .get(ImageListViewModel::class.java)
        ImageListPresenterImpl(
            doorbellData = doorbellData,
            imageListViewModel = viewModel,
            appNavigation = appNavigation
        )
    }
}
