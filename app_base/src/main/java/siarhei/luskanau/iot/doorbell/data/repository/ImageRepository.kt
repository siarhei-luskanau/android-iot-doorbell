package siarhei.luskanau.iot.doorbell.data.repository

import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import java.io.File
import java.io.InputStream

interface ImageRepository {

    fun prepareFile(name: String): File

    fun saveImage(file: File): ImageFile

    fun openInputStream(imageFile: ImageFile): InputStream
}