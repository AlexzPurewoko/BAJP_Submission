package id.apwdevs.app.motvcatalogue.activities

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentAdapter
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentPagedAdapter
import id.apwdevs.app.motvcatalogue.fragment.ContentFragment
import id.apwdevs.app.motvcatalogue.fragment.ParentFragment
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.ItemClickSupport
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : BaseActivity(), ItemClickSupport.OnItemClickListener,
    ContentFragment.ContentFragmentCallback {

    override val countIdlingResource = CountingIdlingResource("MainActivityTEST")
    private val mHandlerVPager: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                tabs?.setScrollPosition(position, positionOffset, true)
            }

            override fun onPageSelected(position: Int) {
                tabs?.getTabAt(position)?.select()
            }

        }

    private val mHandlerTabs: TabLayout.OnTabSelectedListener =
        object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.apply {
                    view_pager.setCurrentItem(position, true)
                }
            }

        }

    val mFragments: MutableList<ParentFragment> = mutableListOf()

    private val mHandlerOffsetAppBar: AppBarLayout.OnOffsetChangedListener =
        AppBarLayout.OnOffsetChangedListener { view, offset ->
            val mRealOffset = abs(offset)
            val mAlpha = 1.0f - (mRealOffset.toFloat() / view.totalScrollRange.toFloat())
            toolbar?.alpha = mAlpha
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        applyFragments(savedInstanceState)
        view_pager.apply {
            adapter = VPagerFragmentStateAdapter(
                supportFragmentManager,
                mFragments
            )
            offscreenPageLimit = 2
            currentItem = 0
        }
        setupTabs()

    }

    private fun applyFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mFragments.addAll(
                listOf(
                    //ContentFragment.newInstance(MainListTabViewModel.ContentDataType.TYPE_MOVIE),
                    //ContentFragment.newInstance(MainListTabViewModel.ContentDataType.TYPE_TV)
                    ParentFragment.newInstance(ParentFragment.Type.HOME),
                    ParentFragment.newInstance(ParentFragment.Type.FAVORITE)
                )
            )
        } else {
            var index = 0
            while (true) {
                val restoredFragment = supportFragmentManager.getFragment(
                    savedInstanceState,
                    "$STORE_FRAGMENT${index++}"
                ) ?: break
                mFragments.add(restoredFragment as ParentFragment)
            }
        }
    }

    override fun isProcessing(process: Boolean) {
        if (process)
            countIdlingResource.increment()
        else if (!process && !countIdlingResource.isIdleNow)
            countIdlingResource.decrement()
    }

    private fun setupTabs() {
        val strTitle = arrayListOf(
            R.string.home_title,
            R.string.favorite_title
        )
        val strImgs = arrayListOf(
            R.drawable.ic_home_24dp,
            R.drawable.ic_favorite_enabled_24dp
        )
        for ((idx, strId) in strTitle.withIndex()) {
            tabs.addTab(
                tabs.newTab().apply {
                    customView =
                        LayoutInflater.from(this@MainActivity)
                            .inflate(R.layout.tab_custom, tabs, false).apply {
                                findViewById<TextView>(R.id.tab_title).setText(strId)
                                findViewById<ImageView>(R.id.tab_icon).apply {
                                    imageTintList = ColorStateList.valueOf(Color.WHITE)
                                    setImageResource(strImgs[idx])
                                }
                            }
                }
            )
        }
    }


    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
        val rawDataContent: DataModel =
            when (val adapter = recyclerView.adapter) {
                is RecyclerContentAdapter -> {
                    adapter.mData[position]
                }
                is RecyclerContentPagedAdapter -> {
                    val d = adapter.getData(position) ?: return
                    DataModel(d.photoRes, d.overview, d.releaseDate, d.titleItem, true)
                }
                else -> return
            }
        val contentDataType = recyclerView.tag
        if (contentDataType is MainListTabViewModel.ContentDataType) {
            Log.e(
                "SELECTED",
                "at position ($position) with type {$contentDataType} and raw data of $rawDataContent"
            )

            startActivity(Intent(this@MainActivity, DetailActivity::class.java).apply {
                putExtras(Bundle().also {
                    it.putParcelable(DetailActivity.EXTRA_DATA, rawDataContent)
                    it.putParcelable(DetailActivity.EXTRA_CONTENT_TYPES, contentDataType)
                    it.putInt(DetailActivity.EXTRA_POSITION_DATA, position)
                })
            })
        }

    }

    override fun onResume() {
        super.onResume()
        view_pager.addOnPageChangeListener(mHandlerVPager)
        tabs.addOnTabSelectedListener(mHandlerTabs)
        main_appbar.addOnOffsetChangedListener(mHandlerOffsetAppBar)
    }

    override fun onPause() {
        super.onPause()
        view_pager.removeOnPageChangeListener(mHandlerVPager)
        tabs.removeOnTabSelectedListener(mHandlerTabs)
        main_appbar.removeOnOffsetChangedListener(mHandlerOffsetAppBar)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        for ((index, fragment) in mFragments.withIndex()) {
            supportFragmentManager.putFragment(outState, "$STORE_FRAGMENT$index", fragment)
        }
    }

    companion object {
        private const val STORE_FRAGMENT = "FRAGMENT_STORE_ID_"
    }

}

private class VPagerFragmentStateAdapter(
    fragmentManager: FragmentManager,
    private val mFragment: List<Fragment>
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = mFragment[position]

    override fun getCount(): Int = mFragment.size

}