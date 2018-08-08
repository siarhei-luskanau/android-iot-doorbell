package siarhei.luskanau.iot.doorbell.persistence

import android.content.Context
import androidx.room.Room
import siarhei.luskanau.iot.doorbell.data.model.ImageData
import siarhei.luskanau.iot.doorbell.data.repository.PersistenceRepository

class DefaultPersistenceRepository(
        context: Context
) : PersistenceRepository {

    private val appDatabase: AppDatabase =
            Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "${context.packageName}.db"
            ).build()

    private val mapper = ImageEntityMapper()

    override fun getImages(
            deviceId: String,
            afterImageId: String?,
            limit: Int
    ) = if (afterImageId != null) {
        mapper.fromEntityList(appDatabase.imageDao().getImages(deviceId, afterImageId, limit))
    } else {
        mapper.fromEntityList(appDatabase.imageDao().getImages(deviceId, limit))
    }


    override fun insertImages(deviceId: String, images: List<ImageData>) =
            appDatabase.imageDao().insertImages(mapper.toEntityList(deviceId, images))

}