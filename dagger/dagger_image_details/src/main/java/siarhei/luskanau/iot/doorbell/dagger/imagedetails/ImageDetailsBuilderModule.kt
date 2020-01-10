package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.LifecycleOwner
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsPresenterImpl

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
    ) = ImageDetailsFragment { args: Bundle?, _: LifecycleOwner ->
        val imageData = commonComponent.provideAppNavigationArgs().getImageDetailsFragmentArgs(args)
        ImageDetailsPresenterImpl(
            imageData = imageData
        )
    }
}
