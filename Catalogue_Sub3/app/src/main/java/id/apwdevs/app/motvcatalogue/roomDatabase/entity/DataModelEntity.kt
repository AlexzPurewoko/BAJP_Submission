package id.apwdevs.app.motvcatalogue.roomDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_data_favorite")
data class DataModelEntity (
    @PrimaryKey
    @ColumnInfo(name = "res_photo")
    val photoRes: Int,
    @ColumnInfo(name = "title_item")
    val titleItem: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "overview_txt")
    val overview: String,
    @ColumnInfo(name = "type_item")
    val type: Int
)