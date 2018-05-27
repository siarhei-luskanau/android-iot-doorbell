package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Observable
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface CameraRepository {

    fun makeImage(
            deviceId: String,
            cameraId: String
    ): Observable<ImageFile>

}