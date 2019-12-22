package id.apwdevs.app.motvcatalogue.viewmodel

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.data.FakeMovieData
import id.apwdevs.app.motvcatalogue.data.FakeRoomDatabase
import id.apwdevs.app.motvcatalogue.data.FakeTvShowsData
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.plugin.AnotherAboutIntoHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class DetailActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mViewModel: DetailActivityViewModel

    @Mock
    private lateinit var mResources: Resources

    @Mock
    private lateinit var loadFinished: Observer<Boolean>

    @Mock
    private lateinit var contentDataModel: Observer<DataModel>

    @Mock
    private lateinit var stateFavoriteCallbacks: Observer<Boolean>

    @Mock
    private lateinit var anotherAbout: Observer<HashMap<String, String>>

    private val dataPosition = 0

    private val fakeRoomDatabase = FakeRoomDatabase.getMockedRoomDatabase()



    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mViewModel = DetailActivityViewModel()
    }

    @Test
    fun testAllWithMovie() {
        loadMovieChecks()
    }

    @Test
    fun testAllWithTvShows() {
        loadTv()
    }

    @Test
    fun checkIsFavorite() {
        val exDataModel = DataModel(FakeMovieData.movieDrawableRes[0], FakeMovieData.movieName[0], FakeMovieData.movieReleasedTimeList[0], FakeMovieData.movieOverview[0], true)
        mViewModel.stateFavoriteCallbacks.observeForever(stateFavoriteCallbacks)
        mViewModel.contentDataModel.value = exDataModel
        mViewModel.checkIsFavorite()

        assertEquals(false, mViewModel.isStateChangedByUser)
        verify(stateFavoriteCallbacks).onChanged(true)
    }

    @Test
    fun clickFavorite() {

        // in this case,
        runBlocking {
            val exDataModel = DataModel(FakeMovieData.movieDrawableRes[0], FakeMovieData.movieName[0], FakeMovieData.movieReleasedTimeList[0], FakeMovieData.movieOverview[0], true)
            mViewModel.stateFavoriteCallbacks.observeForever(stateFavoriteCallbacks)
            mViewModel.contentDataModel.value = exDataModel

            // when delete from database
            `when`(fakeRoomDatabase.getDataModelDao().isItemExists(exDataModel.photoRes)).thenReturn(false)
            mViewModel.clickFavorite(fakeRoomDatabase, Dispatchers.Unconfined)

            verify(fakeRoomDatabase.getDataModelDao()).deleteItem(exDataModel.photoRes)
            verify(stateFavoriteCallbacks).onChanged(false)
            assertEquals(false, exDataModel.isFavorite)
            assertEquals(true, mViewModel.isStateChangedByUser)

            // when click again, the data is added back into database
            mViewModel.contentDataType = MainListTabViewModel.ContentDataType.TYPE_MOVIE
            `when`(fakeRoomDatabase.getDataModelDao().isItemExists(exDataModel.photoRes)).thenReturn(true)
            mViewModel.clickFavorite(fakeRoomDatabase, Dispatchers.Unconfined)

            verify(fakeRoomDatabase.getDataModelDao()).insertToFavorite(listOf(Injection.convertIntoEntity(exDataModel, MainListTabViewModel.ContentDataType.TYPE_MOVIE)))
            verify(stateFavoriteCallbacks).onChanged(true)
            assertEquals(true, exDataModel.isFavorite)
            assertEquals(true, mViewModel.isStateChangedByUser)

        }
    }

    private fun initTestTV(){
        prepareDataTvShows()

        mViewModel.setup(MainListTabViewModel.ContentDataType.TYPE_TV, dataPosition)
        // assert checks
        assertEquals(true, mViewModel.hasFirstInstantiate)
        assertEquals(dataPosition, mViewModel.dataPosition)
        assertEquals(MainListTabViewModel.ContentDataType.TYPE_TV, mViewModel.contentDataType)


    }

    private fun loadTv() {
        runBlocking {

            initTestTV()

            // load all observers
            mViewModel.contentDataModel.observeForever(contentDataModel)
            mViewModel.anotherAbout.observeForever(anotherAbout)
            mViewModel.loadFinished.observeForever(loadFinished)

            // test setUpData Content
            val dataModel = FakeTvShowsData.getExpectedShortData()[dataPosition]
            val sParser = FakeTvShowsData.tvShowsShortAbout[dataPosition]
            val keys = FakeTvShowsData.tvShowsAboutKey.toList()
            mViewModel.setDataModel(dataModel)

            // load test
            mViewModel.load(mResources, Dispatchers.Unconfined)
            verify(loadFinished).onChanged(false)

            // we wait for load process
            delay(1500)

            // verify all mocks
            verify(loadFinished).onChanged(true)
            verify(contentDataModel).onChanged(dataModel)
            verify(anotherAbout).onChanged(AnotherAboutIntoHashMap.intoHashMap(sParser, keys))
        }
    }

    private fun initTestMovie(){
        prepareDataMovie()

        mViewModel.setup(MainListTabViewModel.ContentDataType.TYPE_MOVIE, dataPosition)
        // assert checks
        assertEquals(true, mViewModel.hasFirstInstantiate)
        assertEquals(dataPosition, mViewModel.dataPosition)
        assertEquals(MainListTabViewModel.ContentDataType.TYPE_MOVIE, mViewModel.contentDataType)


    }

    private fun loadMovieChecks() {
        runBlocking {

            initTestMovie()

            // load all observers
            mViewModel.contentDataModel.observeForever(contentDataModel)
            mViewModel.anotherAbout.observeForever(anotherAbout)
            mViewModel.loadFinished.observeForever(loadFinished)

            // test setUpData Content
            val dataModel = FakeMovieData.getExpectedShortData()[dataPosition]
            val sParser = FakeMovieData.movieShortAbout[dataPosition]
            val keys = FakeMovieData.movieShortAboutKey.toList()
            mViewModel.setDataModel(dataModel)

            // load test
            mViewModel.load(mResources, Dispatchers.Unconfined)
            verify(loadFinished).onChanged(false)

            // we wait for load process
            delay(1500)

            // verify all mocks
            verify(loadFinished).onChanged(true)
            verify(contentDataModel).onChanged(dataModel)
            verify(anotherAbout).onChanged(AnotherAboutIntoHashMap.intoHashMap(sParser, keys))
        }
    }


    private fun prepareDataMovie() {
        Mockito.`when`(mResources.getStringArray(R.array.movie_short_about_list)).thenReturn(FakeMovieData.movieShortAbout)
        Mockito.`when`(mResources.getStringArray(R.array.movie_key_short_about_name)).thenReturn(FakeMovieData.movieShortAboutKey)
    }

    private fun prepareDataTvShows() {
        Mockito.`when`(mResources.getStringArray(R.array.tvshows_short_about_lists)).thenReturn(FakeTvShowsData.tvShowsShortAbout)
        Mockito.`when`(mResources.getStringArray(R.array.tvshows_short_about_lists_keys)).thenReturn(FakeTvShowsData.tvShowsAboutKey)
    }
}