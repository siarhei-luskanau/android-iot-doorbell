package siarhei.luskanau.iot.doorbell.persistence

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
        doorbellId: String,
        afterImageId: String?,
        limit: Int
    ): Flow<List<ImageData>> =
        if (afterImageId != null) {
            appDatabase.imageDao().getImages(doorbellId, afterImageId, limit)
        } else {
            appDatabase.imageDao().getImages(doorbellId, limit)
        }
            .map { mapper.fromEntityList(it) }

    override suspend fun insertImages(doorbellId: String, images: List<ImageData>) =
        appDatabase.imageDao().insertImages(mapper.toEntityList(doorbellId, images))
}
