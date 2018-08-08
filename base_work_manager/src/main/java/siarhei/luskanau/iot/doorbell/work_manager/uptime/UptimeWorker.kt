package siarhei.luskanau.iot.doorbell.work_manager.uptime

import androidx.work.Worker
import dagger.android.AndroidInjector
import siarhei.luskanau.iot.doorbell.work_manager.base.BaseWorker

class UptimeWorker : BaseWorker<UptimeWorkerDelegate>() {

    override fun injectThis(injector: AndroidInjector<Worker>): () -> Unit =
            { injector.inject(this) }

    override fun doDelegateWork(): () -> Result =
            { workerDelegate.doWork() }

}