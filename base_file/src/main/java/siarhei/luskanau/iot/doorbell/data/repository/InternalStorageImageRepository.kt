package siarhei.luskanau.iot.doorbell.data.repository

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.ByteBuffer
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

class InternalStorageImageRepository(
    private val context: Context
) : ImageRepository {

    override fun prepareFile(name: String): File =
        File.createTempFile("camera_${name}_", null, context.cacheDir)

    override fun saveImage(byteBuffer: ByteBuffer?, name: String): ImageFile =
        runCatching {
            val file = File.createTempFile("camera_${name}_", null, context.cacheDir)

            val channel = FileOutputStream(file, false).channel
            channel.write(byteBuffer)
            channel.close()

            ImageFile(
                name = name,
                path = file.absolutePath,
                size = file.length()
            )
        }.onFailure {
            ImageFile(throwable = it)
        }.getOrThrow()

    override fun saveImage(file: File): ImageFile =
        runCatching {
            ImageFile(
                name = file.name,
                path = file.absolutePath,
                size = file.length()
            )
        }.onFailure {
            ImageFile(throwable = it)
        }.getOrThrow()

    override fun openInputStream(imageFile: ImageFile): InputStream =
        File(imageFile.path.orEmpty()).inputStream()
}
