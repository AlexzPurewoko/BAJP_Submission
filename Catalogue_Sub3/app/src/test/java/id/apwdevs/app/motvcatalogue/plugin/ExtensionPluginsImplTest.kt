package id.apwdevs.app.motvcatalogue.plugin

import android.content.res.Resources
import android.content.res.TypedArray
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.data.FakeMovieData
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ExtensionPluginsImplTest {

    @Mock
    private lateinit var mResources: Resources

    @Mock
    private lateinit var mTypedArray: TypedArray

    private val exampleListID = FakeMovieData.movieDrawableRes

    private val exampleArrTyped = R.array.movie_drawable_list

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        loadData()
    }

    @Test
    fun getResourceIdTypedArray() {
        val actual = ExtensionPluginsImpl().getResourceIdTypedArray(mResources, exampleArrTyped)

        // check the lists... are they equal?
        for ((index, value) in exampleListID.withIndex()){
            assertEquals(value, actual[index])
        }
        Mockito.verify(mTypedArray).recycle()
        Mockito.verify(mTypedArray).length()

        // check if this method is called?
        for (iD in exampleListID.indices){
            Mockito.verify(mTypedArray).getResourceId(iD, -1)
        }
    }

    private fun loadData() {
        Mockito.`when`(mTypedArray.length()).thenReturn(exampleListID.size)
        for ((iD, value) in exampleListID.withIndex()){
            Mockito.`when`(mTypedArray.getResourceId(iD, -1)).thenReturn(value)
        }
        Mockito.`when`(mResources.obtainTypedArray(exampleArrTyped)).thenReturn(mTypedArray)
    }
}