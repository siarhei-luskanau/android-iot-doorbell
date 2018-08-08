package siarhei.luskanau.iot.doorbell.work_manager.camera

import androidx.work.Worker
import dagger.android.AndroidInjector
import siarhei.luskanau.iot.doorbell.work_manager.base.BaseWorker

class CameraWorker : BaseWorker<CameraWorkerDelegate>() {

    override fun injectThis(injector: AndroidInjector<Worker>): () -> Unit =
            { injector.inject(this) }

    override fun doDelegateWork(): () -> Result =
            { workerDelegate.doWork() }

}