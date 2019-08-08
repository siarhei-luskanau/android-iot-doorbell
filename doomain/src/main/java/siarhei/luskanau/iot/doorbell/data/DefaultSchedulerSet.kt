package siarhei.luskanau.iot.doorbell.data

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

class DefaultSchedulerSet(
    override val io: Scheduler = Schedulers.io(),
    override val ui: Scheduler = AndroidSchedulers.mainThread(),
    override val computation: Scheduler = Schedulers.computation(),
    override val ioCoroutineContext: CoroutineContext = Dispatchers.IO,
    override val uiCoroutineContext: CoroutineContext = Dispatchers.Main
) : SchedulerSet(
    io,
    ui,
    computation,
    ioCoroutineContext,
    uiCoroutineContext
)
