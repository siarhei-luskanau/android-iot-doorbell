package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Completable

interface CameraRepository {

    fun makeAndSendImage(
            deviceId: String,
            cameraId: String
    ): Completable

}