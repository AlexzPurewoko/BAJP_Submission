package id.apwdevs.app.motvcatalogue.repository

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.AnotherAboutIntoHashMap
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPlugins
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import java.util.*

class TvShowsRepository : DataRepository {

    override fun getPagedAllFavorite(roomDatabase: ItemDataDB): LiveData<PagedList<DataModelEntity>> {
        return LivePagedListBuilder(roomDatabase.getDataModelDao().retrievePagedAllTypedFavItem(MainListTabViewModel.ContentDataType.TYPE_TV.id), 6).build()
    }
    override fun isDataAreFavorited(roomDatabase: ItemDataDB, primaryKeyID: Int): Boolean {
        val favDao = roomDatabase.getDataModelDao()
        return favDao.isItemExists(primaryKeyID)
    }

    override fun getAllFavorite(
        roomDatabase: ItemDataDB
    ): List<DataModel> {
        val favDao = roomDatabase.getDataModelDao()
        val retrievedItem = favDao.retrieveAllTypedFavItem(MainListTabViewModel.ContentDataType.TYPE_TV.id)
        val listDMOdel = mutableListOf<DataModel>()
        retrievedItem.forEach {
            listDMOdel.add(
                DataModel(it.photoRes, it.overview, it.releaseDate, it.titleItem, true)
            )
        }
        return listDMOdel.toList()
    }

    override fun getListDataModel(resource: Resources, extensionPlugins: ExtensionPlugins): List<DataModel> {
        val arrTitle : Array<String> = resource.getStringArray(R.array.tvshows_name_lists)
        val arrOverview : Array<String> = resource.getStringArray(R.array.tvshows_overview_lists)
        val arrReleased : Array<String> = resource.getStringArray(R.array.tvshows_released_time_lists)
        val arrTvShows: IntArray = extensionPlugins.getResourceIdTypedArray(resource, R.array.tvshows_drawable_lists)

        val listResult = ArrayList<DataModel>()
        for ((x, title) in arrTitle.withIndex()) {
            listResult.add(
                DataModel(
                    arrTvShows[x],
                    arrOverview[x],
                    arrReleased[x],
                    title
                )
            )
        }

        return listResult.toList()
    }

    override fun getShortAbout(resource: Resources, dataPosition: Int): HashMap<String, String> {
        val loadedShortDetailStr: Array<String> = resource.getStringArray(R.array.tvshows_short_about_lists)
        val loadedShortDetailKey: Array<String> = resource.getStringArray(R.array.tvshows_short_about_lists_keys)

        val rawData = loadedShortDetailStr[dataPosition]
        return AnotherAboutIntoHashMap.intoHashMap(rawData, loadedShortDetailKey.toList())
    }

}