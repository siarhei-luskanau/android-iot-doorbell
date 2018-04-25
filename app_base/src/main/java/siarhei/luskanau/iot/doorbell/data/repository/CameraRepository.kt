package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Observable

interface CameraRepository {

    fun makeImage(
            deviceId: String,
            cameraId: String
    ): Observable<ByteArray>

}