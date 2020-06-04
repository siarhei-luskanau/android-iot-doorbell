package siarhei.luskanau.iot.doorbell.persistence

import siarhei.luskanau.iot.doorbell.data.model.ImageData

class ImageEntityMapper {

    fun toEntityList(deviceId: String, images: List<ImageData>): List<ImageEntity> =
        images.map {
            ImageEntity(
                imageId = it.imageId,
                deviceId = deviceId
            )
        }

    fun fromEntityList(images: List<ImageEntity>): List<ImageData> =
        images.map {
            ImageData(
                imageId = it.imageId,
                imageUri = "imageUri",
                timestampString = "timestampString"
            )
        }
}
