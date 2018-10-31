package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DefaultSchedulerSet(
    override val io: Scheduler = Schedulers.io(),
    override val ui: Scheduler = AndroidSchedulers.mainThread(),
    override val computation: Scheduler = Schedulers.computation()
) : SchedulerSet(io, ui, computation)