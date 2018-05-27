package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import java.io.InputStream
import java.nio.ByteBuffer

interface ImageRepository {

    fun saveImage(byteBuffer: ByteBuffer?, name: String): ImageFile

    fun openInputStream(imageFile: ImageFile): InputStream

}