package siarhei.luskanau.iot.doorbell.dagger.di.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.di.common.FragmentKey
import siarhei.luskanau.iot.doorbell.ui.doorbelllist.DoorbellListFragment
import siarhei.luskanau.iot.doorbell.ui.imagedetails.ImageDetailsFragment
import siarhei.luskanau.iot.doorbell.ui.imagelist.ImageListFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment

@Module
abstract class FragmentBindingModule {

    @Binds
    abstract fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(PermissionsFragment::class)
    abstract fun bindPermissionsFragment(fragment: PermissionsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DoorbellListFragment::class)
    abstract fun bindDoorbellListFragment(fragment: DoorbellListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ImageListFragment::class)
    abstract fun bindImageList(fragment: ImageListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ImageDetailsFragment::class)
    abstract fun bindImageDetailsFragment(fragment: ImageDetailsFragment): Fragment
}
