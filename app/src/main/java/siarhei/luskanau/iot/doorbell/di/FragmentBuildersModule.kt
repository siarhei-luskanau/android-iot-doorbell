package siarhei.luskanau.iot.doorbell.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import siarhei.luskanau.iot.doorbell.ui.doorbells.DoorbellsFragment
import siarhei.luskanau.iot.doorbell.ui.images.ImagesFragment

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    internal abstract fun contributeDoorbellsFragment(): DoorbellsFragment

    @ContributesAndroidInjector
    internal abstract fun contributeImagesFragment(): ImagesFragment

}