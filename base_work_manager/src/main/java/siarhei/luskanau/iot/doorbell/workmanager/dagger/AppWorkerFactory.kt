package siarhei.luskanau.iot.doorbell.workmanager.dagger

import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface AppWorkerFactory<T : ListenableWorker> {
    fun create(params: WorkerParameters): T
}