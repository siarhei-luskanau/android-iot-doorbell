package siarhei.luskanau.iot.doorbell.persistence

import siarhei.luskanau.iot.doorbell.data.model.ImageData

class ImageEntityMapper {

    fun toEntityList(doorbellId: String, images: List<ImageData>): List<ImageEntity> =
        images.map {
            ImageEntity(
                imageId = it.imageId,
                doorbellId = doorbellId
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
