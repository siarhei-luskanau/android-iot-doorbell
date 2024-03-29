package siarhei.luskanau.iot.doorbell.dagger.permissions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.DaggerFragmentFactory
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsFragment
import siarhei.luskanau.iot.doorbell.ui.permissions.PermissionsPresenter
import javax.inject.Provider

@Module
class PermissionsBuilderModule {

    @Provides
    fun providesFragmentFactory(
        providers: MutableMap<Class<out Fragment>, Provider<Fragment>>,
    ): FragmentFactory = DaggerFragmentFactory(
        providers,
    )

    @Provides
    fun providePermissionsFragment(
        appNavigation: AppNavigation,
    ) = PermissionsFragment {
        PermissionsPresenter(appNavigation = appNavigation)
    }
}
