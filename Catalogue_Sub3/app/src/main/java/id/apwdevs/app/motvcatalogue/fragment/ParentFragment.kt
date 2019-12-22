package id.apwdevs.app.motvcatalogue.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import kotlinx.android.parcel.Parcelize

class ParentFragment : Fragment() {
    private var currentContentPosition = 0
    lateinit var currentType : Type
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var mFragments: List<Fragment>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_base_adapter, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState != null) {
            savedInstanceState.apply {
                currentType = requireNotNull(getParcelable(KEY_CONTENT_TYPE))
                currentContentPosition = getInt(KEY_FRAME_CURRENT_POSITION)
                var index = 0
                val list = mutableListOf<Fragment>()
                while (true){
                    val fg = childFragmentManager.getFragment(savedInstanceState, "$KEY_FRAGMENTS${index++}$KEY_AT$currentType") ?: break
                    list.add(fg)
                }
                mFragments = list.toList()
            }
        } else {

            requireNotNull(arguments).apply {
                currentType = requireNotNull(getParcelable(KEY_CONTENT_TYPE))
            }
            // is Favorites?
            val isFav = currentType == Type.FAVORITE
            mFragments = listOf(
                ContentFragment.newInstance(MainListTabViewModel.ContentDataType.TYPE_MOVIE, isFav),
                ContentFragment.newInstance(MainListTabViewModel.ContentDataType.TYPE_TV, isFav)
            )
        }

        // attach a fragment into fragmentManager
        childFragmentManager.beginTransaction().apply {
            for (fragment in mFragments){
                if(savedInstanceState == null)
                    add(R.id.fg_base_frame, fragment)
                hide(fragment)
            }
        }.commit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavigation = view.findViewById(R.id.bottom_nav)
        bottomNavigation.setOnNavigationItemSelectedListener {
            val menu = bottomNavigation.menu
            // check matches and put the value into currentPOsition global variables
            for(index in 0 until menu.size()){
                val sItemMenu = menu[index]
                // break if same
                if(sItemMenu.itemId == it.itemId) {
                    currentContentPosition = index
                    break
                }
            }
            applyFragmentChange()
            true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        for((index, fg) in mFragments.withIndex()){
            childFragmentManager.putFragment(outState, "$KEY_FRAGMENTS$index$KEY_AT$currentType", fg)
        }
        outState.putInt(KEY_FRAME_CURRENT_POSITION, currentContentPosition)
        outState.putParcelable(KEY_CONTENT_TYPE, currentType)
    }


    override fun onResume() {
        super.onResume()
        applyFragmentChange()
    }

    private fun applyFragmentChange() {
        childFragmentManager.beginTransaction().apply {
            // hide the previous fragment
            hide(
                if(currentContentPosition - 1 < 0) mFragments.last()
                else mFragments[currentContentPosition - 1]
            )

            // show the current fragment
            show(mFragments[currentContentPosition])
        }.commit()
    }
    companion object {
        const val KEY_CONTENT_TYPE = "CONTENT_TYPE"
        private const val KEY_FRAME_CURRENT_POSITION = "FRAME_POS"
        private const val KEY_FRAGMENTS = "FRAGMENT_NAME_"
        private const val KEY_AT = "_AT_"

        @JvmStatic
        fun newInstance(contentDisplay: Type) : ParentFragment =
            ParentFragment().apply {
                arguments = Bundle().also {
                    it.putParcelable(KEY_CONTENT_TYPE, contentDisplay)
                }
            }

    }

    @Parcelize
    enum class Type : Parcelable{
        HOME,
        FAVORITE
    }
}