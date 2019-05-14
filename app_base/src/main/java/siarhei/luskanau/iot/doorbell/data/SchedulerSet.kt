package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

open class SchedulerSet(
    open val io: Scheduler,
    open val ui: Scheduler,
    open val computation: Scheduler,
    open val ioCoroutineContext: CoroutineContext,
    open val uiCoroutineContext: CoroutineContext
) {

    companion object {
        @ExperimentalCoroutinesApi
        fun test(): SchedulerSet = SchedulerSet(
                io = TestScheduler(),
                ui = TestScheduler(),
                computation = TestScheduler(),
                ioCoroutineContext = Dispatchers.Unconfined,
                uiCoroutineContext = Dispatchers.Unconfined
        )
    }

    fun triggerActions() {
        (io as TestScheduler?)?.triggerActions()
        (ui as TestScheduler?)?.triggerActions()
        (computation as TestScheduler?)?.triggerActions()
    }
}