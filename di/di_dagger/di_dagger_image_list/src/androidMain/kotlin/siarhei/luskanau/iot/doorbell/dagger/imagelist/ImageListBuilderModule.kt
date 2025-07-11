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
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListViewModel

@Module
class ImageListBuilderModule {

    @Provides
    fun provideFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>
    ): FragmentFactory = DaggerFragmentFactory(
        providers
    )

    @Provides
    fun provideImageListFragment(appNavigation: AppNavigation, commonComponent: CommonComponent) =
        ImageListFragment { fragment: Fragment ->
            val viewModelFactory = ImageListViewModelFactory(
                commonComponent = commonComponent,
                appNavigation = appNavigation,
                args = fragment.arguments
            )
            ViewModelProvider(fragment, viewModelFactory)[ImageListViewModel::class.java]
        }
}
