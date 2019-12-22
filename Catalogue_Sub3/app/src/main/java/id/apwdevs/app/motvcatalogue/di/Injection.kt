package id.apwdevs.app.motvcatalogue.di

import android.content.Context
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.repository.DataRepository
import id.apwdevs.app.motvcatalogue.repository.MovieDataRepository
import id.apwdevs.app.motvcatalogue.repository.TvShowsRepository
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel

object Injection {
    fun getMovieRepository() : DataRepository = MovieDataRepository()

    fun getTvShowsRepository() : DataRepository = TvShowsRepository()

    fun getRoomDatabase(context: Context) : ItemDataDB = ItemDataDB.getInstance(context)

    fun convertIntoEntity(dataModel: DataModel, contentDataType: MainListTabViewModel.ContentDataType) : DataModelEntity =
        DataModelEntity(dataModel.photoRes, dataModel.title.toString(), dataModel.releaseDate.toString(), dataModel.overview.toString(), contentDataType.id)
}