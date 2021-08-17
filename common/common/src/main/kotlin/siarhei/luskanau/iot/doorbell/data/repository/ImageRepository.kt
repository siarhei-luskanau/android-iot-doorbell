package siarhei.luskanau.iot.doorbell.data.repository

import java.io.File
import java.io.InputStream
import java.nio.ByteBuffer
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface ImageRepository {

    fun prepareFile(name: String): File

    fun saveImage(byteBuffer: ByteBuffer?, name: String): ImageFile

    fun saveImage(file: File): ImageFile

    fun openInputStream(imagePath: String): InputStream
}
