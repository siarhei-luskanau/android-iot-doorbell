package siarhei.luskanau.iot.doorbell.dagger.imagelist

import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent
import javax.inject.Provider

@Component(
    modules = [
        ImageListBinderModule::class,
        ImageListBuilderModule::class
    ]
)
interface ImageListComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance appNavigation: AppNavigation,
            @BindsInstance commonComponent: CommonComponent
        ): ImageListComponent
    }
}
