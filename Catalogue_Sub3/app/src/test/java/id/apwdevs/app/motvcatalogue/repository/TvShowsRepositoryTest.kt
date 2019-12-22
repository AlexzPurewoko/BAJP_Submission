package id.apwdevs.app.motvcatalogue.repository

import android.content.res.Resources
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.data.FakeRoomDatabase
import id.apwdevs.app.motvcatalogue.data.FakeTvShowsData
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.AnotherAboutIntoHashMap
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPlugins
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TvShowsRepositoryTest {

    @Mock
    private lateinit var mResources: Resources

    @Mock
    private lateinit var mExtensionPlugins: ExtensionPlugins

    private val dataRepository = Injection.getTvShowsRepository()

    private val fakeRoomDatabase = FakeRoomDatabase.getMockedRoomDatabase()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        prepareDataTvShows()
    }

    @Test
    fun getListDataModel() {
        val listDataModel = dataRepository.getListDataModel(mResources, mExtensionPlugins)
        val expectedData = FakeTvShowsData.getExpectedShortData()
        // check, size is different?
        assertEquals(expectedData.size, listDataModel.size)

        // checks all data in all-loaded data
        for ((index, expectedVal) in expectedData.withIndex()){
            val actualVal = listDataModel[index]
            assertEquals(expectedVal.title, actualVal.title)
            assertEquals(expectedVal.overview, actualVal.overview)
            assertEquals(expectedVal.photoRes, actualVal.photoRes)
            assertEquals(expectedVal.releaseDate, actualVal.releaseDate)
        }
    }

    @Test
    fun getShortAbout() {

        val dataPosition = 0

        val shortActualAboutData = dataRepository.getShortAbout(mResources, dataPosition)
        val expectedAboutData = AnotherAboutIntoHashMap.intoHashMap(FakeTvShowsData.tvShowsShortAbout[dataPosition], FakeTvShowsData.tvShowsAboutKey.toList())

        // check all keys, its matched the size, and contains all that explained in fakeData?
        assertEquals(expectedAboutData.keys.size, shortActualAboutData.keys.size)

        for ((key, value) in expectedAboutData.entries){
            // if not contains the key, will reported!
            assertEquals(true, shortActualAboutData.containsKey(key))

            // check the value
            assertEquals(value, shortActualAboutData[key])
        }
    }

    @Test
    fun getAllFavorite() {
        val expected = FakeRoomDatabase.mListDataEntity.filter { it.type == MainListTabViewModel.ContentDataType.TYPE_TV.id }
        val actual = dataRepository.getAllFavorite(fakeRoomDatabase)

        val dMExpected = mutableListOf<DataModel>()
        expected.forEach {
            dMExpected.add(DataModel(it.photoRes, it.overview, it.releaseDate, it.titleItem, true))
        }

        assertEquals(dMExpected, actual)

    }

    @Test
    fun isDataFavorited() {
        val expected = true
        val primKey = FakeRoomDatabase.mListDataEntity[3].photoRes
        val isFav = dataRepository.isDataAreFavorited(fakeRoomDatabase, primKey)

        assertEquals(expected, isFav)
    }

    private fun prepareDataTvShows() {

        Mockito.`when`(mResources.getStringArray(R.array.tvshows_name_lists)).thenReturn(FakeTvShowsData.tvShowsName)
        Mockito.`when`(mResources.getStringArray(R.array.tvshows_overview_lists)).thenReturn(FakeTvShowsData.tvShowsOverview)
        Mockito.`when`(mResources.getStringArray(R.array.tvshows_released_time_lists)).thenReturn(FakeTvShowsData.tvShowsReleasedTimeList)
        Mockito.`when`(
            mExtensionPlugins.getResourceIdTypedArray(
                mResources,
                R.array.tvshows_drawable_lists
            )
        ).thenReturn(FakeTvShowsData.tvShowsDrawableRes)
        Mockito.`when`(mResources.getStringArray(R.array.tvshows_short_about_lists)).thenReturn(
            FakeTvShowsData.tvShowsShortAbout)
        Mockito.`when`(mResources.getStringArray(R.array.tvshows_short_about_lists_keys)).thenReturn(
            FakeTvShowsData.tvShowsAboutKey)
    }
}