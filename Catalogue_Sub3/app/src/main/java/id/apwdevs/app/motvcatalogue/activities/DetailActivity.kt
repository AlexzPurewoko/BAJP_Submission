package id.apwdevs.app.motvcatalogue.activities

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.espresso.idling.CountingIdlingResource
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentAboutAdapter
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.viewmodel.DetailActivityViewModel
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlin.math.abs

class DetailActivity : BaseActivity() {

    private lateinit var mDetailActivityViewModel: DetailActivityViewModel
    private lateinit var mLoading: Snackbar

    private var menuItem: Menu? = null

    override val countIdlingResource = CountingIdlingResource("DetailActivity")
    var isCollapsed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mLoading = Snackbar.make(activity_detail_view, "", Snackbar.LENGTH_INDEFINITE).also {
            (it.view as FrameLayout).addView(
                LayoutInflater.from(this).inflate(R.layout.load_snackbar, activity_detail_view, false)
            )
        }

        detail_collapse.setOnClickListener {
            app_bar.setExpanded(false, true)
        }

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { p0, p1 ->
            isCollapsed = abs(p1) == p0.totalScrollRange
            Log.e("IS_COLLAPSED", "value -> $isCollapsed")
        })

        mDetailActivityViewModel = ViewModelProviders.of(this).get(DetailActivityViewModel::class.java)
        mDetailActivityViewModel.apply {
            if(!hasFirstInstantiate) {
                requireNotNull(intent.extras).let {
                    val pos = it.getInt(EXTRA_POSITION_DATA)
                    val contentData = it.getParcelable<MainListTabViewModel.ContentDataType>(
                        EXTRA_CONTENT_TYPES
                    )
                    val data = it.getParcelable<DataModel>(EXTRA_DATA)
                    contentData?.let { cData ->
                        setup(cData, pos)
                        setDataModel(data)
                    }
                }
            }

            loadFinished.observe(this@DetailActivity, Observer {
                if(!it) {
                    countIdlingResource.increment()
                    mLoading.show()
                }
                else {
                    mLoading.dismiss()
                    if(!countIdlingResource.isIdleNow)
                        countIdlingResource.decrement()
                }
            })

            anotherAbout.observe(this@DetailActivity, Observer {
                short_about_detail?.apply {
                    adapter = RecyclerContentAboutAdapter(it)
                    layoutManager = LinearLayoutManager(this@DetailActivity)
                }
            })

            contentDataModel.observe(this@DetailActivity, Observer {
                toolbar_layout?.title = it.title
                poster_image?.setImageResource(it.photoRes)
                val releaseDateText = getString(R.string.release_date)
                val spannableString = SpannableStringBuilder().apply {
                    append(releaseDateText, StyleSpan(Typeface.BOLD), 0)
                    append(" : ")
                    append(it.releaseDate, StyleSpan(Typeface.ITALIC), 0)
                    append("\n\n")
                    append(getString(R.string.overview), StyleSpan(Typeface.BOLD), 0)
                    append("\n")
                    append(it.overview)
                }
                detail_overview?.text = spannableString

            })

            stateFavoriteCallbacks.observe(this@DetailActivity, Observer {
                val item = menuItem?.get(0)
                item?.apply {
                    if(itemId == R.id.action_favorite){
                        val snackbarMessage : String = if(it){
                            setIcon(R.drawable.ic_favorite_enabled_24dp)
                            getString(R.string.added_favorite)
                        } else {
                            setIcon(R.drawable.ic_favorite_disabled_24dp)
                            getString(R.string.deleted_favorite)
                        }
                        if(isStateChangedByUser)
                            Snackbar.make(activity_detail_view, snackbarMessage, Snackbar.LENGTH_SHORT).apply {
                                this.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    override fun onDismissed(
                                        transientBottomBar: Snackbar?,
                                        event: Int
                                    ) {
                                        super.onDismissed(transientBottomBar, event)
                                        if(!countIdlingResource.isIdleNow)countIdlingResource.decrement()
                                        removeCallback(this)
                                    }
                                })
                            }.show()
                    }
                }
            })

            load(resources)
        }
    }

    override fun onResume() {
        super.onResume()
        mDetailActivityViewModel.checkIsFavorite()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuItem = menu
        mDetailActivityViewModel.checkIsFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_favorite){
            // change the current favorite state
            mDetailActivityViewModel.clickFavorite(Injection.getRoomDatabase(this))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_POSITION_DATA = "POSITION_DATA"
        const val EXTRA_DATA = "DATA_EXTRA"
        const val EXTRA_CONTENT_TYPES = "EXTRA_CONTENT_DATA_TYPE"
    }
}
