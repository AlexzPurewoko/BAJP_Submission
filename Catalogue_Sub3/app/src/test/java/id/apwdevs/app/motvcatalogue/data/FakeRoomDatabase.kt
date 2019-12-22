package id.apwdevs.app.motvcatalogue.data

import id.apwdevs.app.motvcatalogue.roomDatabase.dao.ItemDataDao
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import org.mockito.Mockito
import org.mockito.Mockito.mock

object FakeRoomDatabase {
    val mListDataEntity = listOf(
        DataModelEntity(FakeMovieData.movieDrawableRes[0], FakeMovieData.movieName[0], FakeMovieData.movieReleasedTimeList[0], FakeMovieData.movieOverview[0], MainListTabViewModel.ContentDataType.TYPE_MOVIE.id),
        DataModelEntity(FakeMovieData.movieDrawableRes[1], FakeMovieData.movieName[1], FakeMovieData.movieReleasedTimeList[1], FakeMovieData.movieOverview[1], MainListTabViewModel.ContentDataType.TYPE_MOVIE.id),
        DataModelEntity(FakeTvShowsData.tvShowsDrawableRes[2], FakeTvShowsData.tvShowsName[2], FakeTvShowsData.tvShowsReleasedTimeList[2], FakeTvShowsData.tvShowsOverview[2], MainListTabViewModel.ContentDataType.TYPE_TV.id),
        DataModelEntity(FakeTvShowsData.tvShowsDrawableRes[3], FakeTvShowsData.tvShowsName[3], FakeTvShowsData.tvShowsReleasedTimeList[3], FakeTvShowsData.tvShowsOverview[3], MainListTabViewModel.ContentDataType.TYPE_TV.id)
    )

    fun getMockedRoomDatabase() :ItemDataDB{

        val itemDataDB = mock(ItemDataDB::class.java)
        // dao
        val itemDao = mock(ItemDataDao::class.java)
        Mockito.`when`(itemDao.retrieveAllFavItem()).thenReturn(mListDataEntity)

        mListDataEntity.forEach {
            Mockito.`when`(itemDao.isItemExists(it.photoRes)).thenReturn(true)
        }
        Mockito.`when`(itemDao.retrieveAllTypedFavItem(MainListTabViewModel.ContentDataType.TYPE_MOVIE.id))
            .thenReturn(mListDataEntity.filter { it.type == MainListTabViewModel.ContentDataType.TYPE_MOVIE.id })

        Mockito.`when`(itemDao.retrieveAllTypedFavItem(MainListTabViewModel.ContentDataType.TYPE_TV.id))
            .thenReturn(mListDataEntity.filter { it.type == MainListTabViewModel.ContentDataType.TYPE_TV.id })


        Mockito.`when`(itemDataDB.getDataModelDao()).thenReturn(itemDao)
        return itemDataDB
    }

    fun getAllFavDataModel() {}
}