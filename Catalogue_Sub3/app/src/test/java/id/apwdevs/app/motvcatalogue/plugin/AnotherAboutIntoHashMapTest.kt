package id.apwdevs.app.motvcatalogue.plugin

import org.junit.Test

import org.junit.Assert.*

class AnotherAboutIntoHashMapTest {

    private val sParser = "English|2h 2m|$170,000,000.00|$404,852,543.00|Action,Science Fiction,Thriller,Adventure"
    private val keys = listOf("Original Language", "Runtime", "Budget", "Revenue", "Genres")
    private val fakeHashMap : HashMap<String, String> = hashMapOf(
        keys[0] to "English",
        keys[1] to "2h 2m",
        keys[2] to "$170,000,000.00",
        keys[3] to "$404,852,543.00",
        keys[4] to "Action,Science Fiction,Thriller,Adventure"
    )
    @Test
    fun intoHashMap() {
        val actualData = AnotherAboutIntoHashMap.intoHashMap(sParser, keys)
        assertEquals(fakeHashMap, actualData)
    }
}