package siarhei.luskanau.iot.doorbell.common.test

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import siarhei.luskanau.iot.doorbell.data.SchedulerSet

@ExperimentalCoroutinesApi
class TestSchedulerSet(
    override val io: Scheduler = TestScheduler(),
    override val ui: Scheduler = TestScheduler(),
    override val ioCoroutineContext: CoroutineContext = TestCoroutineDispatcher(),
    override val uiCoroutineContext: CoroutineContext = TestCoroutineDispatcher()
) : SchedulerSet(
    io,
    ui,
    ioCoroutineContext,
    uiCoroutineContext
)
