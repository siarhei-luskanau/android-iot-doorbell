package siarhei.luskanau.iot.doorbell.di.common

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import siarhei.luskanau.iot.doorbell.AppActivity

@Module
class AppActivityModule {

    @Provides
    fun provideAppCompatActivity(
        activity: AppActivity
    ): AppCompatActivity =
            activity
}