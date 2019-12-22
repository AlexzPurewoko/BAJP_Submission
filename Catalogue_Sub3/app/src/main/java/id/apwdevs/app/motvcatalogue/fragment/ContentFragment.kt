package id.apwdevs.app.motvcatalogue.fragment

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentAdapter
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentPagedAdapter
import id.apwdevs.app.motvcatalogue.di.Injection
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.ExtensionPluginsImpl
import id.apwdevs.app.motvcatalogue.plugin.ItemClickSupport
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import kotlinx.coroutines.Dispatchers
import kotlin.math.roundToInt

class ContentFragment : Fragment(){

    private var mainTabViewModel : MainListTabViewModel? = null
    private lateinit var mRecyclerViewContent: RecyclerView
    private lateinit var mSwipeRefresh: SwipeRefreshLayout
    private lateinit var mTextNoResult: TextView

    private var mOnItemClickListener : ItemClickSupport.OnItemClickListener? = null
    private var mContentFragmentProcessing : ContentFragmentCallback? = null

    val contentType: MainListTabViewModel.ContentDataType?
    get() = mainTabViewModel?.contentDataType


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainTabViewModel = ViewModelProviders.of(this).get(MainListTabViewModel::class.java)
        mainTabViewModel?.apply {
            val point = Point()
            requireActivity().windowManager.defaultDisplay.getSize(point)
            requireNotNull(arguments).apply {
                val contentDataType = requireNotNull(getParcelable<MainListTabViewModel.ContentDataType>(KEY_CONTENT_TYPE))
                contentIsFavorite = getBoolean(KEY_CONTENT_IS_FAVORITE)
                if(!hasFirstInstantiate)
                    setup(contentDataType, point)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_main_content, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerViewContent = view.findViewById(R.id.recycler_content_list)
        mSwipeRefresh = view as SwipeRefreshLayout
        mTextNoResult = view.findViewById(R.id.fg_content_noresult)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainTabViewModel?.apply {
            isLoading.observe(this@ContentFragment, Observer {
                mSwipeRefresh.isRefreshing = it
                mContentFragmentProcessing?.isProcessing(it)
            })

            data.observe(this@ContentFragment, Observer {
                setupRecycler(it)
            })

            if(contentIsFavorite){
                getPagedListData(Injection.getRoomDatabase(requireContext())).observe(this@ContentFragment, Observer {
                    if(it.isNullOrEmpty())showNoResult()
                    else
                        mTextNoResult.visibility = View.GONE

                    if(mRecyclerViewContent.adapter !is RecyclerContentPagedAdapter){
                        val point = Point(
                            resources.getDimension(R.dimen.item_poster_width).roundToInt(),
                            resources.getDimension(R.dimen.item_poster_height).roundToInt()
                        )
                        mRecyclerViewContent.adapter = RecyclerContentPagedAdapter(point)
                        mRecyclerViewContent.layoutManager = LinearLayoutManager(requireContext())
                        mRecyclerViewContent.setHasFixedSize(true)

                        ItemClickSupport.addTo(mRecyclerViewContent)?.onItemClickListener = object : ItemClickSupport.OnItemClickListener {
                            override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                                recyclerView.tag = contentType
                                mOnItemClickListener?.onItemClicked(recyclerView, position, v)
                            }

                        }
                    }

                    val mAdapter = mRecyclerViewContent.adapter as RecyclerContentPagedAdapter
                    mAdapter.submitList(it)
                    mAdapter.notifyDataSetChanged()

                })
            }
            loadData(resources, Injection.getRoomDatabase(requireContext()), ExtensionPluginsImpl(), Dispatchers.IO)
        }
        mSwipeRefresh.setOnRefreshListener {
            mainTabViewModel?.loadData(resources, Injection.getRoomDatabase(requireContext()), ExtensionPluginsImpl(), Dispatchers.IO)
        }
    }

    private fun setupRecycler(list: List<DataModel>?) {
        if(list.isNullOrEmpty()) showNoResult()
        else
            mTextNoResult.visibility = View.GONE
        ItemClickSupport.removeFrom(mRecyclerViewContent)

        list?.let {
            val point = Point(
                resources.getDimension(R.dimen.item_poster_width).roundToInt(),
                resources.getDimension(R.dimen.item_poster_height).roundToInt()
            )

            mRecyclerViewContent.apply {
                adapter = RecyclerContentAdapter(it, point)
                layoutManager = LinearLayoutManager(requireContext())
            }

            ItemClickSupport.addTo(mRecyclerViewContent)?.onItemClickListener = object : ItemClickSupport.OnItemClickListener {
                override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
                    recyclerView.tag = contentType
                    mOnItemClickListener?.onItemClicked(recyclerView, position, v)
                }

            }
        }
    }

    private fun showNoResult() {
        mTextNoResult.setText(R.string.no_result)
        mTextNoResult.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mOnItemClickListener = context as ItemClickSupport.OnItemClickListener
        mContentFragmentProcessing = context as ContentFragmentCallback
    }

    override fun onDetach() {
        super.onDetach()
        mOnItemClickListener = null
        mContentFragmentProcessing = null
    }

    interface ContentFragmentCallback {
        fun isProcessing(process: Boolean)
    }

    companion object {
        const val KEY_CONTENT_TYPE = "CONTENT_TYPE"
        const val KEY_CONTENT_IS_FAVORITE = "CONTENT_FAV"

        @JvmStatic
        fun newInstance(fragmentType: MainListTabViewModel.ContentDataType, contentIsFavorite: Boolean = false) : ContentFragment =
            ContentFragment().apply {
                arguments = Bundle().also {
                    it.putParcelable(KEY_CONTENT_TYPE, fragmentType)
                    it.putBoolean(KEY_CONTENT_IS_FAVORITE, contentIsFavorite)
                }
            }
    }
}
