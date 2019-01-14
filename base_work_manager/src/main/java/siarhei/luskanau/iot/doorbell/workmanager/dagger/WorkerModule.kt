package siarhei.luskanau.iot.doorbell.workmanager.dagger

import androidx.work.ListenableWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.workmanager.CameraWorker
import siarhei.luskanau.iot.doorbell.workmanager.UptimeWorker

@Module
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(CameraWorker::class)
    fun bindCameraWorker(factory: CameraWorker.Factory): AppWorkerFactory<out ListenableWorker>

    @Binds
    @IntoMap
    @WorkerKey(UptimeWorker::class)
    fun bindUptimeWorker(factory: UptimeWorker.Factory): AppWorkerFactory<out ListenableWorker>
}