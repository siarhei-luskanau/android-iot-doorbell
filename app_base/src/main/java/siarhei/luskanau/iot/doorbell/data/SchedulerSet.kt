package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

open class SchedulerSet(
        open val io: Scheduler,
        open val ui: Scheduler,
        open val computation: Scheduler
) {

    companion object {
        fun test(): SchedulerSet = SchedulerSet(
                TestScheduler(),
                TestScheduler(),
                TestScheduler()
        )
    }

    fun triggerActions() {
        (io as TestScheduler?)?.triggerActions()
        (ui as TestScheduler?)?.triggerActions()
        (computation as TestScheduler?)?.triggerActions()
    }

}