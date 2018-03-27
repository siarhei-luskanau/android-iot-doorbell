package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface DoorbellRepository {

    fun listenDoorbellsList(): Flowable<List<DoorbellData>>

    fun listenCamerasList(deviceId: String): Flowable<List<CameraData>>

    fun listenImagesList(deviceId: String): Flowable<List<ImageData>>

    fun sendDoorbellData(doorbellData: DoorbellData): Completable

    fun sendCamerasList(deviceId: String, list: List<CameraData>): Completable

    fun sendCameraImageRequest(deviceId: String, cameraId: String, isRequested: Boolean): Completable

    fun listenCameraImageRequest(deviceId: String): Flowable<Map<String, Boolean>>

}