package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

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
