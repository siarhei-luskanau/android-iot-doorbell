package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.common.FragmentKey
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.slide.ImageDetailsSlideFragment

@Module
interface ImageDetailsBinderModule {

    @Binds
    @IntoMap
    @FragmentKey(ImageDetailsFragment::class)
    fun bindImageDetailsFragment(fragment: ImageDetailsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ImageDetailsSlideFragment::class)
    fun bindImageDetailsSlideFragment(fragment: ImageDetailsSlideFragment): Fragment
}
