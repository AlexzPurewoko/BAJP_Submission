package id.apwdevs.app.motvcatalogue.roomDatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import id.apwdevs.app.motvcatalogue.roomDatabase.dao.ItemDataDao
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity

@Database(entities = [DataModelEntity::class], version = 1, exportSchema = false)
abstract class ItemDataDB : RoomDatabase() {

    abstract fun getDataModelDao() : ItemDataDao

    companion object {
        private var instance : ItemDataDB? = null

        fun getInstance(context: Context): ItemDataDB =
            instance ?: Room.databaseBuilder(context.applicationContext, ItemDataDB::class.java, "item_fav").build().apply { instance = this }
    }
}