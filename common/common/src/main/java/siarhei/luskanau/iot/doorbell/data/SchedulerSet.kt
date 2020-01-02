package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import kotlin.coroutines.CoroutineContext

open class SchedulerSet(
    open val io: Scheduler,
    open val ui: Scheduler,
    open val ioCoroutineContext: CoroutineContext,
    open val uiCoroutineContext: CoroutineContext
)
