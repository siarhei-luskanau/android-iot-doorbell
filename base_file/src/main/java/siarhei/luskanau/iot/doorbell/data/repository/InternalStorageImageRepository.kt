package siarhei.luskanau.iot.doorbell.data.repository

import android.content.Context
import siarhei.luskanau.iot.doorbell.data.model.ImageFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.ByteBuffer

class InternalStorageImageRepository(
    val context: Context
) : ImageRepository {

    override fun saveImage(byteBuffer: ByteBuffer?, name: String): ImageFile =
            try {
                val file = createFile(name)

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

    override fun openInputStream(imageFile: ImageFile): InputStream =
            File(imageFile.path.orEmpty()).inputStream()

    private fun createFile(name: String) = File.createTempFile("camera_${name}_", null, context.cacheDir)
}