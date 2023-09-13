package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragmentArgs
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl
import javax.inject.Provider

@Module
class ImageDetailsBuilderModule {

    @Provides
    fun providesFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>,
    ): FragmentFactory = DaggerFragmentFactory(
        providers,
    )

    @Provides
    fun provideImageDetailsFragment(
        commonComponent: CommonComponent,
    ) = ImageDetailsFragment { fragment: Fragment ->
        val doorbellId = ImageDetailsFragmentArgs.fromBundle(
            requireNotNull(fragment.arguments),
        ).doorbellId
        val imageId = ImageDetailsFragmentArgs.fromBundle(
            requireNotNull(fragment.arguments),
        ).imageId
        ImageDetailsPresenterImpl(
            doorbellId = doorbellId,
            imageId = imageId,
            doorbellRepository = commonComponent.provideDoorbellRepository(),
        )
    }
}
