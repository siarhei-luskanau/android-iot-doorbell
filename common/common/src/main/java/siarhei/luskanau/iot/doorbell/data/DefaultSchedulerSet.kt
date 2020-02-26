package siarhei.luskanau.iot.doorbell.data

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

class DefaultSchedulerSet(
    override val ioCoroutineContext: CoroutineContext = Dispatchers.IO,
    override val uiCoroutineContext: CoroutineContext = Dispatchers.Main
) : SchedulerSet(
    ioCoroutineContext,
    uiCoroutineContext
)
