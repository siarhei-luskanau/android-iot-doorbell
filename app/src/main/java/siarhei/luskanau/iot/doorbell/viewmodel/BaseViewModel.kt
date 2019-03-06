package siarhei.luskanau.iot.doorbell.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    protected val disposables: CompositeDisposable = CompositeDisposable()
    protected val jobs = mutableListOf<Job>()

    override fun onCleared() {
        disposables.clear()
        jobs.forEach {
            it.cancel()
            jobs.remove(it)
        }
        super.onCleared()
    }
}