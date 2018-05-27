package siarhei.luskanau.iot.doorbell

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import siarhei.luskanau.iot.doorbell.data.repository.CameraRepository
import siarhei.luskanau.iot.doorbell.data.repository.DoorbellRepository
import siarhei.luskanau.iot.doorbell.data.repository.ThisDeviceRepository
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

class CameraService : Service() {

    @Inject
    lateinit var thisDeviceRepository: ThisDeviceRepository
    @Inject
    lateinit var doorbellRepository: DoorbellRepository
    @Inject
    lateinit var cameraRepository: CameraRepository
    private val scheduler = Schedulers.from(Executors.newSingleThreadExecutor())
    private var disposable: Disposable? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
        Timber.d("CameraService: onCreate()")

        disposable = doorbellRepository.listenCameraImageRequest(thisDeviceRepository.doorbellId())
                .firstOrError()
                .flatMapCompletable { requestMap: Map<String, Boolean>? ->
                    val filteredRequestMap: Map<String, Boolean> = requestMap.orEmpty()
                            .filter { entry: Map.Entry<String, Boolean> ->
                                entry.value
                            }

                    if (filteredRequestMap.isEmpty()) {
                        Completable.fromAction {
                            Timber.d("ImageRequests is empty")
                        }
                    } else if (thisDeviceRepository.isPermissionsGranted()) {
                        Completable.merge(filteredRequestMap.map { entry: Map.Entry<String, Boolean> ->
                            Timber.d("makeAndSendImage:%s", entry.key)
                            makeAndSendImage(entry.key)
                        })
                    } else {
                        Completable.fromAction {
                            Timber.d("Permissions is not granted")
                            startActivity(Intent(this, PermissionActivity::class.java))
                        }
                    }
                }
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(
                        { stopSelf() },
                        {
                            Timber.e(it)
                            stopSelf()
                        }
                )
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("CameraService: onDestroy()")
        disposable?.dispose()
        disposable = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return Service.START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun makeAndSendImage(cameraId: String): Completable =
            doorbellRepository.sendCameraImageRequest(
                    deviceId = thisDeviceRepository.doorbellId(),
                    cameraId = cameraId,
                    isRequested = false
            ).andThen(
                    cameraRepository
                            .makeImage(
                                    deviceId = thisDeviceRepository.doorbellId(),
                                    cameraId = cameraId
                            )
                            .filter { it.size ?: 0 > 0 }
                            .flatMap { imageFile: ImageFile ->
                                doorbellRepository.sendImage(
                                        deviceId = thisDeviceRepository.doorbellId(),
                                        cameraId = cameraId,
                                        imageFile = imageFile
                                )
                                        .toObservable<Nothing>()
                            }
                            .ignoreElements()
            )

}
