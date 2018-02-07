package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class SchedulerSet(
        val io: Scheduler = Schedulers.io(),
        val ui: Scheduler = AndroidSchedulers.mainThread(),
        val computation: Scheduler = Schedulers.computation()
) {

    companion object {
        fun test(): SchedulerSet = SchedulerSet(
                TestScheduler(),
                TestScheduler(),
                TestScheduler()
        )
    }

    fun triggerActions() {
        if (io is TestScheduler) {
            io.triggerActions()
        }
        if (ui is TestScheduler) {
            ui.triggerActions()
        }
        if (computation is TestScheduler) {
            computation.triggerActions()
        }
    }

}