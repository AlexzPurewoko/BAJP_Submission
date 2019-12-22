package id.apwdevs.app.motvcatalogue.repository

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPlugins
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity

/**
 * This is used by class repository to get the data...
 * @author Alexzander Purwoko Widiantoro
 */


interface DataRepository {

    /**
     * Get the data from @param resource with the plugin explained and return it into
     * the type of DataModel
     */
    fun getListDataModel(resource: Resources, extensionPlugins: ExtensionPlugins) : List<DataModel>

    /**
     * Get the HashMap of pair (key,value) from sParsed data with the position...
     */
    fun getShortAbout(resource: Resources, dataPosition: Int) : HashMap<String, String>

    /**
     * Get all of content favorite in the room database
     */
    fun getAllFavorite(roomDatabase: ItemDataDB) : List<DataModel>

    fun getPagedAllFavorite(roomDatabase: ItemDataDB) : LiveData<PagedList<DataModelEntity>>

    fun isDataAreFavorited(roomDatabase: ItemDataDB, primaryKeyID: Int) : Boolean


}