package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlidePresenterImpl
import javax.inject.Provider

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
        appNavigation: AppNavigation
    ) = ImageDetailsFragment { fragment: Fragment ->
        val doorbellId = ImageDetailsFragmentArgs.fromBundle(
            requireNotNull(fragment.arguments)
        ).doorbellId
        val imageId = ImageDetailsFragmentArgs.fromBundle(
            requireNotNull(fragment.arguments)
        ).imageId
        ImageDetailsPresenterImpl(
            appNavigation = appNavigation,
            fragment = fragment,
            doorbellId = doorbellId,
            imageId = imageId
        )
    }

    @Provides
    fun provideImageDetailsSlideFragment(
        commonComponent: CommonComponent
    ) =
        ImageDetailsSlideFragment { fragment: Fragment ->
            val doorbellId = ImageDetailsFragmentArgs.fromBundle(
                requireNotNull(fragment.arguments)
            ).doorbellId
            val imageId = ImageDetailsFragmentArgs.fromBundle(
                requireNotNull(fragment.arguments)
            ).imageId
            ImageDetailsSlidePresenterImpl(
                doorbellId = doorbellId,
                imageId = imageId,
                doorbellRepository = commonComponent.provideDoorbellRepository()
            )
        }
}
