package siarhei.luskanau.iot.doorbell.data.repository

import java.io.InputStream

interface ImageRepository {

    fun saveInputStream(inputStream: InputStream, name: String): String

    fun openInputStream(name: String): InputStream

    fun deleteFile(fileName: String): Boolean

}