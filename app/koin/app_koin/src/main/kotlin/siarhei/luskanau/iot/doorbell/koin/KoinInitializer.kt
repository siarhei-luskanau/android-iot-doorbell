package siarhei.luskanau.iot.doorbell.koin

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import siarhei.luskanau.iot.doorbell.koin.di.appModule
import siarhei.luskanau.iot.doorbell.koin.doorbelllist.di.doorbellListModule
import siarhei.luskanau.iot.doorbell.koin.imagedetails.di.imageDetailsModule
import siarhei.luskanau.iot.doorbell.koin.imagelist.di.imageListModule
import siarhei.luskanau.iot.doorbell.koin.permissions.di.permissionsModule
import siarhei.luskanau.iot.doorbell.koin.splash.di.splashModule

class KoinInitializer : Initializer<KoinApplication> {

    override fun create(context: Context): KoinApplication =
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(context)
            modules(
                listOf(
                    appModule,
                    doorbellListModule,
                    imageDetailsModule,
                    imageListModule,
                    permissionsModule,
                    splashModule
                )
            )
        }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        emptyList()
}
