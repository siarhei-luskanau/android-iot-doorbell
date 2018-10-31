package siarhei.luskanau.iot.doorbell.workmanager.dagger

import dagger.Subcomponent
import dagger.android.AndroidInjector
import siarhei.luskanau.iot.doorbell.workmanager.uptime.UptimeWorker

@Subcomponent
interface WorkerSubcomponent : AndroidInjector<UptimeWorker> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<UptimeWorker>()
}