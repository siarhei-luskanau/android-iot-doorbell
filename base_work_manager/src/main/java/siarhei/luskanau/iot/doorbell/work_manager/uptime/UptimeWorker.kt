package siarhei.luskanau.iot.doorbell.work_manager.uptime

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import dagger.android.AndroidInjector
import siarhei.luskanau.iot.doorbell.work_manager.base.BaseWorker

class UptimeWorker(
        context: Context,
        workerParams: WorkerParameters
) : BaseWorker<UptimeWorkerDelegate>(
        context,
        workerParams
) {

    override fun injectThis(injector: AndroidInjector<Worker>): () -> Unit =
            { injector.inject(this) }

    override fun doDelegateWork(): () -> Result =
            { workerDelegate.doWork() }

}