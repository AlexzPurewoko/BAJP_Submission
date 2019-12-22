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

class MovieDataRepository : DataRepository {
    override fun getPagedAllFavorite(roomDatabase: ItemDataDB): LiveData<PagedList<DataModelEntity>> {
        return LivePagedListBuilder(roomDatabase.getDataModelDao().retrievePagedAllTypedFavItem(MainListTabViewModel.ContentDataType.TYPE_MOVIE.id), 6).build()
    }

    override fun isDataAreFavorited(roomDatabase: ItemDataDB, primaryKeyID: Int): Boolean {
        val favDao = roomDatabase.getDataModelDao()
        return favDao.isItemExists(primaryKeyID)
    }

    override fun getAllFavorite(
        roomDatabase: ItemDataDB
    ): List<DataModel> {
        val favDao = roomDatabase.getDataModelDao()
        val retrievedItem = favDao.retrieveAllTypedFavItem(MainListTabViewModel.ContentDataType.TYPE_MOVIE.id)
        val listDMOdel = mutableListOf<DataModel>()
        retrievedItem.forEach {
            listDMOdel.add(
                DataModel(it.photoRes, it.overview, it.releaseDate, it.titleItem, true)
            )
        }
        return listDMOdel.toList()
    }

    override fun getListDataModel(resource: Resources, extensionPlugins: ExtensionPlugins): List<DataModel> {
        val arrTitle : Array<String> = resource.getStringArray(R.array.movie_name_list)
        val arrOverview : Array<String> = resource.getStringArray(R.array.movie_overview_str_list)
        val arrReleased : Array<String> = resource.getStringArray(R.array.movie_released_time_list)
        val arrMoviePoster: IntArray = extensionPlugins.getResourceIdTypedArray(resource, R.array.movie_drawable_list)

        val listResult = ArrayList<DataModel>()
        for ((x, title) in arrTitle.withIndex()) {
            listResult.add(
                DataModel(
                    arrMoviePoster[x],
                    arrOverview[x],
                    arrReleased[x],
                    title
                )
            )
        }
        return listResult.toList()
    }

    override fun getShortAbout(resource: Resources, dataPosition: Int): HashMap<String, String> {
        val loadedShortDetailStr: Array<String> = resource.getStringArray(R.array.movie_short_about_list)
        val loadedShortDetailKey: Array<String> = resource.getStringArray(R.array.movie_key_short_about_name)

        val rawData = loadedShortDetailStr[dataPosition]
        return AnotherAboutIntoHashMap.intoHashMap(rawData, loadedShortDetailKey.toList())
    }
}