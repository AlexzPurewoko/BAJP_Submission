package id.apwdevs.app.motvcatalogue.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.res.Resources
import android.graphics.Point
import android.provider.ContactsContract
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.data.FakeMovieData
import id.apwdevs.app.motvcatalogue.data.FakeRoomDatabase
import id.apwdevs.app.motvcatalogue.data.FakeRoomDatabase.mListDataEntity
import id.apwdevs.app.motvcatalogue.data.FakeShortAboutData
import id.apwdevs.app.motvcatalogue.data.FakeTvShowsData
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPlugins
import id.apwdevs.app.motvcatalogue.repository.DataRepository
import id.apwdevs.app.motvcatalogue.roomDatabase.dao.ItemDataDao
import id.apwdevs.app.motvcatalogue.roomDatabase.database.ItemDataDB
import id.apwdevs.app.motvcatalogue.roomDatabase.entity.DataModelEntity
import org.junit.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MainListTabViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var mViewModelTest: MainListTabViewModel

    @Mock
    private lateinit var mResources: Resources

    @Mock
    private lateinit var mDataObserver: Observer<List<DataModel>>

    @Mock
    private lateinit var mIsLoading: Observer<Boolean>

    @Mock
    private lateinit var mExtensionPlugins: ExtensionPlugins

    @Mock
    private lateinit var point: Point

    private val fakeRoomDatabase = FakeRoomDatabase.getMockedRoomDatabase()


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mViewModelTest = MainListTabViewModel()
    }

    @Test
    fun checkInit() {
        prepareResourcesMovie()
        mViewModelTest.isLoading.observeForever(mIsLoading)
        mViewModelTest.setup(MainListTabViewModel.ContentDataType.TYPE_MOVIE, point)

        // checks all
        assertEquals(true, mViewModelTest.hasFirstInstantiate)
        assertEquals(point, mViewModelTest.windowSize)
        assertEquals(MainListTabViewModel.ContentDataType.TYPE_MOVIE, mViewModelTest.contentDataType)
        //verify(mIsLoading).onChanged(false)
    }

    @Test
    fun checkInitTvShows() {
        prepareResourcesTvShows()
        mViewModelTest.isLoading.observeForever(mIsLoading)
        mViewModelTest.setup(MainListTabViewModel.ContentDataType.TYPE_TV, point)

        // checks all
        assertEquals(true, mViewModelTest.hasFirstInstantiate)
        assertEquals(point, mViewModelTest.windowSize)
        assertEquals(MainListTabViewModel.ContentDataType.TYPE_TV, mViewModelTest.contentDataType)
        //verify(mIsLoading).onChanged(false)
    }

    @Test
    fun loadDataMovie() {
        runBlocking {
            checkInit()
            mViewModelTest.data.observeForever(mDataObserver)

            mViewModelTest.loadData(mResources, fakeRoomDatabase, mExtensionPlugins, Dispatchers.Unconfined)

            verify(mIsLoading).onChanged(true)

            delay(2000)
            val expectedShortData = (FakeMovieData as FakeShortAboutData).getExpectedShortData()
            val filteredData = mListDataEntity.filter { it.type == MainListTabViewModel.ContentDataType.TYPE_MOVIE.id }
            expectedShortData.forEach {
                for (exData in filteredData){
                    if(it.photoRes == exData.photoRes) {
                        it.isFavorite = true
                        break
                    }
                }
            }

            verify(mDataObserver).onChanged(expectedShortData)
            verify(mIsLoading).onChanged(false)

        }
    }

    @Test
    fun loadDataTvShows() {
        runBlocking {
            checkInitTvShows()
            mViewModelTest.data.observeForever(mDataObserver)

            mViewModelTest.loadData(mResources, fakeRoomDatabase, mExtensionPlugins, Dispatchers.Unconfined)

            verify(mIsLoading).onChanged(true)

            delay(2000)
            val expectedShortData = (FakeTvShowsData as FakeShortAboutData).getExpectedShortData()
            val filteredData = mListDataEntity.filter { it.type == MainListTabViewModel.ContentDataType.TYPE_TV.id }
            expectedShortData.forEach {
                for (exData in filteredData){
                    if(it.photoRes == exData.photoRes) {
                        it.isFavorite = true
                        break
                    }
                }
            }
            verify(mDataObserver).onChanged(expectedShortData)
            verify(mIsLoading).onChanged(false)

        }
    }

    @Test
    @Suppress("UNCHECKED")
    fun getPagedListData() {
        val dataRepository = mock(DataRepository::class.java)
        val liveData = MutableLiveData<PagedList<DataModelEntity>>()
        val pagedList : PagedList<DataModelEntity> = mock(PagedList::class.java) as PagedList<DataModelEntity>
        liveData.value = pagedList
        val observer : Observer<PagedList<DataModelEntity>> = mock(Observer::class.java) as Observer<PagedList<DataModelEntity>>
        `when`(dataRepository.getPagedAllFavorite(fakeRoomDatabase)).thenReturn(liveData)
        mViewModelTest.setup(MainListTabViewModel.ContentDataType.TYPE_MOVIE, mock(Point::class.java), dataRepository)
        mViewModelTest.getPagedListData(fakeRoomDatabase).observeForever(observer)

        verify(observer).onChanged(pagedList)
    }

    private fun prepareResourcesTvShows() {
        `when`(mResources.getStringArray(R.array.tvshows_name_lists)).thenReturn(FakeTvShowsData.tvShowsName)
        `when`(mResources.getStringArray(R.array.tvshows_overview_lists)).thenReturn(FakeTvShowsData.tvShowsOverview)
        `when`(mResources.getStringArray(R.array.tvshows_released_time_lists)).thenReturn(FakeTvShowsData.tvShowsReleasedTimeList)
        `when`(mExtensionPlugins.getResourceIdTypedArray(mResources, R.array.tvshows_drawable_lists)).thenReturn(FakeTvShowsData.tvShowsDrawableRes)

    }

    private fun prepareResourcesMovie() {
        `when`(mResources.getStringArray(R.array.movie_name_list)).thenReturn(FakeMovieData.movieName)
        `when`(mResources.getStringArray(R.array.movie_overview_str_list)).thenReturn(FakeMovieData.movieOverview)
        `when`(mResources.getStringArray(R.array.movie_released_time_list)).thenReturn(FakeMovieData.movieReleasedTimeList)
        `when`(mExtensionPlugins.getResourceIdTypedArray(mResources, R.array.movie_drawable_list)).thenReturn(FakeMovieData.movieDrawableRes)
    }

}