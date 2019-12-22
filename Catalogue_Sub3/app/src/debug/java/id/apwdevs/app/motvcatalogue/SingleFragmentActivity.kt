package id.apwdevs.app.motvcatalogue

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource
import id.apwdevs.app.motvcatalogue.fragment.ContentFragment
import id.apwdevs.app.motvcatalogue.plugin.ItemClickSupport
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SingleFragmentActivity : AppCompatActivity(), ItemClickSupport.OnItemClickListener,
    ContentFragment.ContentFragmentCallback {


    var onItemClick: ItemClickSupport.OnItemClickListener? = null
    val countIdlingResource = CountingIdlingResource("TestActivity")
    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
        onItemClick?.onItemClicked(recyclerView, position, v)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            FrameLayout(this).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
                )
                id = R.id.container
            }
        )
    }

    override fun isProcessing(process: Boolean) {
        if (process)
            countIdlingResource.increment()
        else if(!countIdlingResource.isIdleNow)
            countIdlingResource.decrement()
    }

    fun setFragment(fragment: Fragment) {
        isProcessing(true)
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment, "TEST")
            .commit()

        GlobalScope.launch {
            while (fragment.isVisible) delay(100)
            isProcessing(false)
        }
    }

}