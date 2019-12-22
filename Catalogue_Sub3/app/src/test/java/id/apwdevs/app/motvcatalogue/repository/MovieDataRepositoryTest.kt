package id.apwdevs.app.motvcatalogue.repository

import android.content.res.Resources
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.data.FakeMovieData
import id.apwdevs.app.motvcatalogue.data.FakeRoomDatabase
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.AnotherAboutIntoHashMap
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPlugins
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MovieDataRepositoryTest {


    @Mock
    private lateinit var mResources: Resources

    @Mock
    private lateinit var mExtensionPlugins: ExtensionPlugins

    private val dataRepository = Injection.getMovieRepository()

    private val fakeRoomDatabase = FakeRoomDatabase.getMockedRoomDatabase()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        prepareDataMovie()
    }

    @Test
    fun getListDataModel() {
        val listDataModel = dataRepository.getListDataModel(mResources, mExtensionPlugins)
        val expectedData = FakeMovieData.getExpectedShortData()
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
        val expectedAboutData = AnotherAboutIntoHashMap.intoHashMap(FakeMovieData.movieShortAbout[dataPosition], FakeMovieData.movieShortAboutKey.toList())

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
        val expected = FakeRoomDatabase.mListDataEntity.filter { it.type == MainListTabViewModel.ContentDataType.TYPE_MOVIE.id }
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
        val primKey = FakeRoomDatabase.mListDataEntity[0].photoRes
        val isFav = dataRepository.isDataAreFavorited(fakeRoomDatabase, primKey)

        assertEquals(expected, isFav)
    }

    private fun prepareDataMovie() {
        Mockito.`when`(mResources.getStringArray(R.array.movie_name_list)).thenReturn(FakeMovieData.movieName)
        Mockito.`when`(mResources.getStringArray(R.array.movie_overview_str_list)).thenReturn(FakeMovieData.movieOverview)
        Mockito.`when`(mResources.getStringArray(R.array.movie_released_time_list)).thenReturn(FakeMovieData.movieReleasedTimeList)
        Mockito.`when`(
            mExtensionPlugins.getResourceIdTypedArray(
                mResources,
                R.array.movie_drawable_list
            )
        ).thenReturn(FakeMovieData.movieDrawableRes)
        Mockito.`when`(mResources.getStringArray(R.array.movie_short_about_list)).thenReturn(
            FakeMovieData.movieShortAbout)
        Mockito.`when`(mResources.getStringArray(R.array.movie_key_short_about_name)).thenReturn(
            FakeMovieData.movieShortAboutKey)
    }
}