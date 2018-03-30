package siarhei.luskanau.iot.doorbell.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.SchedulerSet
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import timber.log.Timber
import javax.inject.Inject

class DoorbellsViewModel @Inject constructor(
        schedulerSet: SchedulerSet,
        doorbellRepository: DoorbellRepository
) : ViewModel() {

    val doorbellsLiveData: LiveData<List<DoorbellData>> =
            LiveDataReactiveStreams.fromPublisher(
                    doorbellRepository.listenDoorbellsList()
                            .doOnError { Timber.e(it) }
                            .onErrorResumeNext(Flowable.empty())
                            .subscribeOn(schedulerSet.io)
                            .observeOn(schedulerSet.ui)
            )

}
