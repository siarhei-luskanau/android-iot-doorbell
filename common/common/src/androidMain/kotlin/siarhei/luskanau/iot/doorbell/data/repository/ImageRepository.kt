package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import java.io.File
import java.nio.ByteBuffer

interface ImageRepository {

    fun prepareFile(name: String): File

    fun saveImage(byteBuffer: ByteBuffer?, name: String): ImageFile

    fun saveImage(file: File): ImageFile
}
