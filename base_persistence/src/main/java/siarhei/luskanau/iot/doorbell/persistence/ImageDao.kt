package siarhei.luskanau.iot.doorbell.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {

    @Query(
        "SELECT * FROM images " +
                "WHERE device_id = :deviceId " +
                "ORDER BY image_id DESC " +
                "LIMIT :limit"
    )
    suspend fun getImages(
        deviceId: String,
        limit: Int
    ): List<ImageEntity>

    @Query(
        "SELECT * FROM images " +
                "WHERE device_id = :deviceId " +
                "AND image_id > :afterImageId " +
                "ORDER BY image_id DESC " +
                "LIMIT :limit"
    )
    suspend fun getImages(
        deviceId: String,
        afterImageId: String,
        limit: Int
    ): List<ImageEntity>

    @Insert
    suspend fun insertImages(images: List<ImageEntity>)
}