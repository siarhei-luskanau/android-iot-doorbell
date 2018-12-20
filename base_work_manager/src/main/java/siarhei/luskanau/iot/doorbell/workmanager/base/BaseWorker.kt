package siarhei.luskanau.iot.doorbell.workmanager.base

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.android.AndroidInjector
import siarhei.luskanau.iot.doorbell.workmanager.dagger.HasWorkerInjector
import timber.log.Timber
import javax.inject.Inject

abstract class BaseWorker<T : Any>(
    context: Context,
    workerParams: WorkerParameters
) : Worker(
        context,
        workerParams
) {

    @Inject
    protected lateinit var workerDelegate: T

    override fun doWork(): Result =
            try {
                if (applicationContext !is HasWorkerInjector) {
                    throw RuntimeException("${applicationContext.javaClass.canonicalName} does not implement ${HasWorkerInjector::class.java.canonicalName}")
                }

                val workerInjector = (applicationContext as HasWorkerInjector).workerInjector()
                checkNotNull(workerInjector) { "${applicationContext.javaClass}.workerInjector() return null" }
                injectThis(workerInjector).invoke()

                doDelegateWork().invoke()
            } catch (throwable: Throwable) {
                Timber.e(throwable)

                Result.failure()
            }

    protected abstract fun injectThis(injector: AndroidInjector<Worker>): () -> Unit

    protected abstract fun doDelegateWork(): () -> Result
}