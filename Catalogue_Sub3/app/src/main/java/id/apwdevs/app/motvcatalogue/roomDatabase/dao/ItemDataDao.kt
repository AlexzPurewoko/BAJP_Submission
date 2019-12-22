package id.apwdevs.app.motvcatalogue.roomDatabase.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity

@Dao
interface ItemDataDao {
    companion object {
        const val tableName = "item_data_favorite"
    }

    @Query("SELECT * FROM $tableName")
    fun retrieveAllFavItem() : List<DataModelEntity>

    @Query("SELECT * FROM $tableName WHERE type_item LIKE :itemType")
    fun retrieveAllTypedFavItem(itemType: Int) : List<DataModelEntity>

    @Query("SELECT * FROM $tableName WHERE type_item LIKE :itemType")
    fun retrievePagedAllTypedFavItem(itemType: Int) : DataSource.Factory<Int, DataModelEntity>

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM $tableName WHERE res_photo LIKE :primaryKeyId)THEN CAST(1 AS BIT)ELSE CAST(0 AS BIT) END AS BOOL")
    fun isItemExists(primaryKeyId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertToFavorite(items: List<DataModelEntity>)

    @Query("DELETE FROM $tableName WHERE res_photo LIKE :primaryKeyId")
    fun deleteItem(primaryKeyId: Int)

}