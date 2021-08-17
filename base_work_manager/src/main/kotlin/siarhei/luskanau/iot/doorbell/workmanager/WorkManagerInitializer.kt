package siarhei.luskanau.iot.doorbell.workmanager

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.WorkManager

class WorkManagerInitializer : Initializer<WorkManager> {

    override fun create(context: Context): WorkManager {
        val workerFactoryProvider = context.applicationContext as WorkerFactoryProvider
        val workerFactory = workerFactoryProvider.provideWorkerFactory()
        val config = Configuration.Builder().setWorkerFactory(workerFactory).build()
        WorkManager.initialize(context, config)
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        emptyList()
}
