package siarhei.luskanau.iot.doorbell.dagger.permissions

import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation

@Component(
    modules = [
        PermissionsBinderModule::class,
        PermissionsBuilderModule::class
    ]
)
interface PermissionsComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance appNavigation: AppNavigation): PermissionsComponent
    }
}
