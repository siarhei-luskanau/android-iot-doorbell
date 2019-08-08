package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

    fun triggerRxJavaActions() {
        (io as TestScheduler?)?.triggerActions()
        (ui as TestScheduler?)?.triggerActions()
        (computation as TestScheduler?)?.triggerActions()
    }
}
