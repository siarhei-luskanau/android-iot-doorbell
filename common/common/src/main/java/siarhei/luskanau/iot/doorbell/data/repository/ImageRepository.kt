package siarhei.luskanau.iot.doorbell.data.repository

import android.net.Uri
import java.io.File
import java.io.InputStream
import java.nio.ByteBuffer
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

interface ImageRepository {

    fun prepareFile(name: String): File

    fun saveImage(byteBuffer: ByteBuffer?, name: String): ImageFile

    fun saveImage(imageUri: Uri?, file: File): ImageFile

    fun openInputStream(imageFile: ImageFile): InputStream
}
