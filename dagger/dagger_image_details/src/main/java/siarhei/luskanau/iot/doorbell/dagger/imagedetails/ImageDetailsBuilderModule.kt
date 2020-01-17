package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
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
        appNavigation: AppNavigation
    ) = ImageDetailsFragment { fragment: Fragment ->
        val doorbellData = fragment.arguments?.let { args ->
            ImageDetailsFragmentArgs.fromBundle(args).doorbellData
        }
        val imageData = fragment.arguments?.let { args ->
            ImageDetailsFragmentArgs.fromBundle(args).imageData
        }
        ImageDetailsPresenterImpl(
            appNavigation = appNavigation,
            fragment = fragment,
            doorbellData = doorbellData,
            imageData = imageData
        )
    }

    @Provides
    fun provideImageDetailsSlideFragment() =
        ImageDetailsSlideFragment { fragment: Fragment ->
            val imageData = fragment.arguments?.let { args ->
                ImageDetailsFragmentArgs.fromBundle(args).imageData
            }
            ImageDetailsSlidePresenterImpl(
                imageData = imageData
            )
        }
}
