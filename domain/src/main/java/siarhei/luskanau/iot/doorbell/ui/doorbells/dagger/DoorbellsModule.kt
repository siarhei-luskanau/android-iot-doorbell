package siarhei.luskanau.iot.doorbell.ui.doorbells.dagger

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import siarhei.luskanau.android.framework.interactor.ISchedulerSet

@Module
class DoorbellsModule {

    @Provides
    internal fun provideSchedulerSet(): ISchedulerSet {
        return object : ISchedulerSet {

            private val subscribeOn = Schedulers.io()
            private val observeOn = Schedulers.computation()

            override fun subscribeOn(): Scheduler {
                return subscribeOn
            }

            override fun observeOn(): Scheduler {
                return observeOn
            }
        }
    }
}
