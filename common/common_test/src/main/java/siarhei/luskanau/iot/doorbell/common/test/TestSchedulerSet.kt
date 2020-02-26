package siarhei.luskanau.iot.doorbell.common.test

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import siarhei.luskanau.iot.doorbell.data.SchedulerSet

@ExperimentalCoroutinesApi
class TestSchedulerSet(
    override val ioCoroutineContext: CoroutineContext = TestCoroutineDispatcher(),
    override val uiCoroutineContext: CoroutineContext = TestCoroutineDispatcher()
) : SchedulerSet(
    ioCoroutineContext,
    uiCoroutineContext
)
