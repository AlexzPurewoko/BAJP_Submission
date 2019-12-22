package id.apwdevs.app.motvcatalogue.viewmodel

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.repository.DataRepository
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import kotlinx.coroutines.*

class DetailActivityViewModel : ViewModel(){
    var hasFirstInstantiate : Boolean = false
    val loadFinished: MutableLiveData<Boolean> = MutableLiveData()
    val contentDataModel: MutableLiveData<DataModel> = MutableLiveData()
    val anotherAbout: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    var contentDataType: MainListTabViewModel.ContentDataType? = null
    var dataPosition: Int = 0

    val stateFavoriteCallbacks : MutableLiveData<Boolean> = MutableLiveData()

    @Volatile
    var isStateChangedByUser = false

    fun setup(contentDataType: MainListTabViewModel.ContentDataType, dataPosition: Int){
        this.contentDataType = contentDataType
        this.dataPosition = dataPosition
        hasFirstInstantiate = true
    }

    fun setDataModel(dataModel: DataModel?){
        contentDataModel.postValue(dataModel)
    }

    fun load(resources: Resources, dispatcher: CoroutineDispatcher = Dispatchers.Main){
        if(!hasFirstInstantiate || loadFinished.value == false) return
        GlobalScope.launch(dispatcher) {
            loadFinished.postValue(false)

            val dataRepository : DataRepository = when(contentDataType){
                MainListTabViewModel.ContentDataType.TYPE_MOVIE -> {
                    Injection.getMovieRepository()
                }
                MainListTabViewModel.ContentDataType.TYPE_TV -> {
                    Injection.getTvShowsRepository()
                }
                null -> return@launch
            }
            anotherAbout.postValue(
                dataRepository.getShortAbout(resources, dataPosition)
            )
            delay(1200)
            loadFinished.postValue(true)
        }
    }
    fun checkIsFavorite(){
        isStateChangedByUser = false
        stateFavoriteCallbacks.postValue(contentDataModel.value?.isFavorite)
    }

    fun clickFavorite(itemDataDB: ItemDataDB, dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        GlobalScope.launch(dispatcher){
            contentDataModel.value?.apply {
                loadFinished.postValue(false)
                val currentState = isFavorite
                val dao = itemDataDB.getDataModelDao()
                if(currentState) {
                    dao.deleteItem(photoRes)
                } else {
                    dao.insertToFavorite(listOf(Injection.convertIntoEntity(this, contentDataType ?: return@launch)))
                }
                isFavorite = dao.isItemExists(photoRes)
                isStateChangedByUser = true
                // post the new value
                stateFavoriteCallbacks.postValue(isFavorite)
                loadFinished.postValue(true)
            }
        }
    }

}