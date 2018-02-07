package siarhei.luskanau.iot.doorbell.companion.images2

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import siarhei.luskanau.iot.doorbell.ImageEntry
import siarhei.luskanau.iot.doorbell.repository.ImageRepository
import javax.inject.Inject


class ImagesViewModel @Inject constructor(
        val imageRepository: ImageRepository
) : ViewModel() {

    private lateinit var deviceId: String
    private var data: LiveData<List<ImageEntry>> = MutableLiveData<List<ImageEntry>>()
    private val disposables = CompositeDisposable()

    fun init(deviceId: String) {
        this.deviceId = deviceId
    }

    fun getImages(): LiveData<List<ImageEntry>> {
        return data
    }

    fun loadImages() {
        imageRepository.toString()
//        disposables.add(loadGreetingUseCase.execute()
//                .subscribeOn(schedulersFacade.io())
//                .observeOn(schedulersFacade.ui())
//                .doOnSubscribe({ __ -> response.setValue(Response.loading()) })
//                .subscribe(
//                        { greeting -> response.setValue(Response.success(greeting)) },
//                        { throwable -> response.setValue(Response.error(throwable)) }
//                )
//        )
    }

    override fun onCleared() {
        disposables.clear()
    }
}