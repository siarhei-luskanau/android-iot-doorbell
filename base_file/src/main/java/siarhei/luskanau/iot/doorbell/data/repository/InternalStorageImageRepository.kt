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
            try {
                val file = File.createTempFile("camera_${name}_", null, context.cacheDir)

                val channel = FileOutputStream(file, false).channel
                channel.write(byteBuffer)
                channel.close()

                ImageFile(
                        name = name,
                        path = file.absolutePath,
                        size = file.length()
                )
            } catch (t: Throwable) {
                ImageFile(throwable = t)
            }

    override fun saveImage(file: File): ImageFile =
        try {
            ImageFile(
                name = file.name,
                path = file.absolutePath,
                size = file.length()
            )
        } catch (t: Throwable) {
            ImageFile(throwable = t)
        }

    override fun openInputStream(imageFile: ImageFile): InputStream =
        File(imageFile.path.orEmpty()).inputStream()
}
