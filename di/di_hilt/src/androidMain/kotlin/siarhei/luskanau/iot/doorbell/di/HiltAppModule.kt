package siarhei.luskanau.iot.doorbell.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import siarhei.luskanau.iot.doorbell.common.DeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.AndroidDeviceInfoProvider
import siarhei.luskanau.iot.doorbell.data.repository.ImageRepository
import siarhei.luskanau.iot.doorbell.data.repository.InternalStorageImageRepository

@Module
@InstallIn(SingletonComponent::class)
internal object HiltAppModule {
    @Provides
    fun provideImageRepository(context: Context): ImageRepository = InternalStorageImageRepository(
        context = context
    )

    @Provides
    fun provideDeviceInfoProvider(context: Context): DeviceInfoProvider = AndroidDeviceInfoProvider(
        context = context
    )
}
