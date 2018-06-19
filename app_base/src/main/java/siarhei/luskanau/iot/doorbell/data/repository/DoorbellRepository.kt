package siarhei.luskanau.iot.doorbell.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import siarhei.luskanau.iot.doorbell.data.model.CameraData
import siarhei.luskanau.iot.doorbell.data.model.DoorbellData
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface DoorbellRepository {

    fun listenDoorbellsList(size: Int, startAt: String? = null): Single<List<DoorbellData>>

    fun listenCamerasList(deviceId: String): Flowable<List<CameraData>>

    fun listenImagesList(deviceId: String, size: Int, startAt: String? = null): Single<List<ImageData>>

    fun sendDoorbellData(doorbellData: DoorbellData): Completable

    fun sendCamerasList(deviceId: String, list: List<CameraData>): Completable

    fun sendCameraImageRequest(deviceId: String, cameraId: String, isRequested: Boolean): Completable

    fun listenCameraImageRequest(deviceId: String): Flowable<Map<String, Boolean>>

    fun sendImage(deviceId: String, cameraId: String, imageFile: ImageFile): Completable

}