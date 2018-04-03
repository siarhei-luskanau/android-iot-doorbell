package siarhei.luskanau.iot.doorbell.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Observable
import timber.log.Timber
import java.io.ByteArrayOutputStream

class ImageCompressor {

    fun scale(sourceObservable: Observable<ByteArray>, maxSize: Int): Observable<ByteArray> =
            sourceObservable.map { bytes ->
                try {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

                    val inSampleSize = calculateInSampleSize(options, maxSize, maxSize)
                    // some times the input bytes size is very big
                    val maxImageBytes = options.outHeight * options.outWidth
                    if (inSampleSize > 1 || bytes.size > maxImageBytes) {
                        options.inSampleSize = inSampleSize
                        options.inJustDecodeBounds = false

                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                        val outputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                        return@map outputStream.toByteArray()
                    }
                } catch (t: Throwable) {
                    Timber.e(t)
                }
                return@map bytes
            }

    companion object {

        private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2

                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }

            return inSampleSize
        }

    }
}