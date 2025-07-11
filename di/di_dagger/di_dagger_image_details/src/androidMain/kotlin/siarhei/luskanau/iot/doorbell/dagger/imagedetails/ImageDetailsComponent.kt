package siarhei.luskanau.iot.doorbell.dagger.imagedetails

import androidx.fragment.app.FragmentFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Provider
import siarhei.luskanau.iot.doorbell.common.AppNavigation
import siarhei.luskanau.iot.doorbell.dagger.common.CommonComponent

@Component(
    modules = [
        ImageDetailsBinderModule::class,
        ImageDetailsBuilderModule::class
    ]
)
interface ImageDetailsComponent {

    fun provideFragmentFactory(): Provider<FragmentFactory>

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance appNavigation: AppNavigation,
            @BindsInstance commonComponent: CommonComponent
        ): ImageDetailsComponent
    }
}
