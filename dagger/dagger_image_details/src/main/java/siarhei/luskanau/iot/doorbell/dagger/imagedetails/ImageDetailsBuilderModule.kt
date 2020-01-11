package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl

@Module
class ImageDetailsBuilderModule {

    @Provides
    fun providesFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>
    ): FragmentFactory = DaggerFragmentFactory(
        providers
    )

    @Provides
    fun provideImageDetailsFragment(
        commonComponent: CommonComponent
    ) = ImageDetailsFragment { fragment: Fragment ->
        val doorbellData = commonComponent.provideAppNavigationArgs()
            .getDoorbellDataImageDetailsFragmentArgs(fragment.arguments)
        val imageData = commonComponent.provideAppNavigationArgs()
            .getImageDataImageDetailsFragmentArgs(fragment.arguments)
        ImageDetailsPresenterImpl(
            appNavigationArgs = commonComponent.provideAppNavigationArgs(),
            fragment = fragment,
            doorbellData = doorbellData,
            imageData = imageData
        )
    }

    @Provides
    fun provideImageDetailsSlideFragment(
        commonComponent: CommonComponent
    ) = ImageDetailsSlideFragment { fragment: Fragment ->
        val imageData = commonComponent.provideAppNavigationArgs()
            .getImageDataImageDetailsFragmentArgs(fragment.arguments)
        ImageDetailsSlidePresenterImpl(
            imageData = imageData
        )
    }
}
