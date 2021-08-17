package siarhei.luskanau.iot.doorbell.dagger.imagelist

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.common.FragmentKey
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment

@Module
interface ImageListBinderModule {

    @Binds
    @IntoMap
    @FragmentKey(ImageListFragment::class)
    fun bindImageListFragment(fragment: ImageListFragment): Fragment
}
