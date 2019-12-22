package id.apwdevs.app.motvcatalogue.plugin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat

class RecyclerViewItemAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        if(noViewFoundException != null) throw noViewFoundException
        view?.apply {
            if(this is RecyclerView){
                assertNotNull(adapter)
                assertThat(adapter?.itemCount ?: 0, `is`(expectedCount))
            }
        }
    }

}