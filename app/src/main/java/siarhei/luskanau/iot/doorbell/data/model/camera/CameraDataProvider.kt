package siarhei.luskanau.iot.doorbell.data.model.camera

import siarhei.luskanau.iot.doorbell.data.model.CameraData

interface CameraDataProvider {

    fun getCamerasList(): List<CameraData>

}