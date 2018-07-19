package siarhei.luskanau.iot.doorbell.work_manager.camera

import androidx.work.Worker
import siarhei.luskanau.iot.doorbell.work_manager.base.WorkerDelegate
import javax.inject.Inject

class CameraWorkerDelegate @Inject constructor(
) : WorkerDelegate {

    override fun doWork(): Worker.Result =
            Worker.Result.SUCCESS

}