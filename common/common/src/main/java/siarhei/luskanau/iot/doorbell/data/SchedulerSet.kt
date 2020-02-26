package siarhei.luskanau.iot.doorbell.data

import kotlin.coroutines.CoroutineContext

open class SchedulerSet(
    open val ioCoroutineContext: CoroutineContext,
    open val uiCoroutineContext: CoroutineContext
)
