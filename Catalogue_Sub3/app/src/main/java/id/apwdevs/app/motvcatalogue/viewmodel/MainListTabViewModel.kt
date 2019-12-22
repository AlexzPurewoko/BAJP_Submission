package id.apwdevs.app.motvcatalogue.viewmodel

import android.content.res.Resources
import android.graphics.Point
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPlugins
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPluginsImpl
import id.apwdevs.app.motvcatalogue.repository.DataRepository
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class
MainListTabViewModel : ViewModel() {
    internal val data: MutableLiveData<List<DataModel>> = MutableLiveData()
    internal val isLoading: MutableLiveData<Boolean> = MutableLiveData()


    lateinit var windowSize : Point
    internal var contentDataType: ContentDataType? = null
    var contentIsFavorite : Boolean = false
    var hasFirstInstantiate: Boolean = false

    private lateinit var dataRepository : DataRepository

    fun setup(contentDataType: ContentDataType, windowSize: Point, dataRepository: DataRepository? = null){
        this.contentDataType = contentDataType
        this.windowSize = windowSize
        hasFirstInstantiate = true

        this.dataRepository =
            dataRepository
                ?: when(contentDataType){
                    ContentDataType.TYPE_MOVIE -> {
                        Injection.getMovieRepository()
                    }
                    ContentDataType.TYPE_TV -> {
                        Injection.getTvShowsRepository()
                    }
                }

    }
    fun loadData(resources: Resources, itemDataDB: ItemDataDB, extensionPlugins: ExtensionPlugins = ExtensionPluginsImpl(), dispatcher: CoroutineDispatcher= Dispatchers.IO){
        isLoading.postValue(true)
        GlobalScope.launch(dispatcher) {

            val favDao = itemDataDB.getDataModelDao()
            if(!contentIsFavorite) {
                val allData = dataRepository.getListDataModel(resources, extensionPlugins)
                // check all, is this favorite?
                allData.forEach {
                    it.isFavorite = favDao.isItemExists(it.photoRes)
                }
                data.postValue(allData)
            }
            isLoading.postValue(false)
        }

    }

    fun getPagedListData(itemDataDB: ItemDataDB) : LiveData<PagedList<DataModelEntity>> {
        return dataRepository.getPagedAllFavorite(itemDataDB)
    }
    @Parcelize
    enum class ContentDataType(val id: Int) : Parcelable {
        TYPE_MOVIE(0x4f),
        TYPE_TV(0xab)
    }
}
