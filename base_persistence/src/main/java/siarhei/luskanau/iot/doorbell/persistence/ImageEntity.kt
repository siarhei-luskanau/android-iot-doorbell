package siarhei.luskanau.iot.doorbell.persistence

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(

        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "row_id")
        val rowId: Int? = null,

        @ColumnInfo(name = "image_id")
        val imageId: String,

        @ColumnInfo(name = "device_id")
        val deviceId: String

)