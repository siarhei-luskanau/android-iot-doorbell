package siarhei.luskanau.iot.doorbell

import android.app.Application
import androidx.startup.AppInitializer
import androidx.work.WorkerFactory
import siarhei.luskanau.iot.doorbell.workmanager.WorkerFactoryProvider
import timber.log.Timber

class AppApplication :
    Application(),
    WorkerFactoryProvider {

    private val diHolder by lazy {
        AppInitializer.getInstance(this).initializeComponent(DiInitializer::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        diHolder.onAppCreate(this)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        diHolder.onAppTrimMemory(this)
    }

    override fun provideWorkerFactory(): WorkerFactory = diHolder.provideWorkerFactory()
}
