package siarhei.luskanau.iot.doorbell.ui.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    private val jobs = mutableListOf<Job>()
    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        jobs.forEach { it.cancel() }
        jobs.clear()
        disposables.clear()
        super.onCleared()
    }

    fun Job.cancelOnCleared() {
        jobs.add(this)
    }

    fun Disposable.disposeOnCleared() {
        disposables.add(this)
    }
}
