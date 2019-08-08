package siarhei.luskanau.iot.doorbell.data.repository

import android.content.Context
import java.io.File
import java.io.InputStream
import siarhei.luskanau.iot.doorbell.data.model.ImageFile

class InternalStorageImageRepository(
    private val context: Context
) : ImageRepository {

    override fun prepareFile(name: String): File =
        File.createTempFile("camera_${name}_", null, context.cacheDir)

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
