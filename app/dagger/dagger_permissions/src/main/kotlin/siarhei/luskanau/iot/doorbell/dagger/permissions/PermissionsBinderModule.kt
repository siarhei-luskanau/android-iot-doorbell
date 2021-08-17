package siarhei.luskanau.iot.doorbell.dagger.permissions

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.dagger.common.FragmentKey
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment

@Module
interface PermissionsBinderModule {

    @Binds
    @IntoMap
    @FragmentKey(PermissionsFragment::class)
    fun bindPermissionsFragment(fragment: PermissionsFragment): Fragment
}
