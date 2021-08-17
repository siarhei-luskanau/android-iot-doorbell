package siarhei.luskanau.iot.doorbell.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query(
        "SELECT * FROM images " +
            "WHERE doorbell_id = :doorbellId " +
            "ORDER BY image_id DESC " +
            "LIMIT :limit"
    )
    fun getImages(
        doorbellId: String,
        limit: Int
    ): Flow<List<ImageEntity>>

    @Query(
        "SELECT * FROM images " +
            "WHERE doorbell_id = :doorbellId " +
            "AND image_id > :afterImageId " +
            "ORDER BY image_id DESC " +
            "LIMIT :limit"
    )
    fun getImages(
        doorbellId: String,
        afterImageId: String,
        limit: Int
    ): Flow<List<ImageEntity>>

    @Insert
    suspend fun insertImages(images: List<ImageEntity>)
}
