package siarhei.luskanau.iot.doorbell.dagger.doorbelllist

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent

@Component(
    modules = [
        DoorbellListBinderModule::class,
        DoorbellListBuilderModule::class
    ]
)
interface DoorbellListComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindFragmentActivity(fragmentActivity: FragmentActivity): Builder

        @BindsInstance
        fun bindAppNavigation(appNavigation: AppNavigation): Builder

        @BindsInstance
        fun bindCommonComponent(commonComponent: CommonComponent): Builder

        fun build(): DoorbellListComponent
    }
}
