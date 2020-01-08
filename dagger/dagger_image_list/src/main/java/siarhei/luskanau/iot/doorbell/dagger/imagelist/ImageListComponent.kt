package siarhei.luskanau.iot.doorbell.dagger.imagelist

import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent

@Component(
    modules = [
        ImageListBinderModule::class,
        ImageListBuilderModule::class
    ]
)
interface ImageListComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun bindAppNavigation(appNavigation: AppNavigation): Builder

        @BindsInstance
        fun bindCommonComponent(commonComponent: CommonComponent): Builder

        fun build(): ImageListComponent
    }
}
