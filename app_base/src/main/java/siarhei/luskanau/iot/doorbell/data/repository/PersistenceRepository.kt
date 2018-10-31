package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.ImageData

interface PersistenceRepository {

    fun getImages(deviceId: String, afterImageId: String?, limit: Int): List<ImageData>

    fun insertImages(deviceId: String, images: List<ImageData>)
}