package siarhei.luskanau.iot.doorbell.work_manager

import androidx.work.Worker
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import siarhei.luskanau.iot.doorbell.work_manager.camera.CameraWorker
import siarhei.luskanau.iot.doorbell.work_manager.dagger.WorkerKey
import siarhei.luskanau.iot.doorbell.work_manager.uptime.UptimeWorker

@Module(subcomponents = [
    WorkerSubcomponent::class,
    CameraWorkerSubcomponent::class
])
interface WorkerModule {

    @Binds
    @IntoMap
    @WorkerKey(UptimeWorker::class)
    fun bindUptimeWorker(builder: WorkerSubcomponent.Builder): AndroidInjector.Factory<out Worker>

    @Binds
    @IntoMap
    @WorkerKey(CameraWorker::class)
    fun bindCameraWorker(builder: CameraWorkerSubcomponent.Builder): AndroidInjector.Factory<out Worker>

}

@Subcomponent
interface WorkerSubcomponent : AndroidInjector<UptimeWorker> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<UptimeWorker>()
}

@Subcomponent
interface CameraWorkerSubcomponent : AndroidInjector<CameraWorker> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<CameraWorker>()
}