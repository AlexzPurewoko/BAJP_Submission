package id.apwdevs.app.motvcatalogue.fragmentTest

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.SingleFragmentActivity
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentAdapter
import id.apwdevs.app.motvcatalogue.dataset.FakeMovieData
import id.apwdevs.app.motvcatalogue.fragment.ContentFragment
import id.apwdevs.app.motvcatalogue.plugin.ItemClickSupport
import id.apwdevs.app.motvcatalogue.plugin.RecyclerViewItemAssertion
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class ContentFragmentTest {

    @get:Rule
    val activityRule: ActivityTestRule<SingleFragmentActivity> = ActivityTestRule(SingleFragmentActivity::class.java)

    private val contentFragment = ContentFragment.newInstance(MainListTabViewModel.ContentDataType.TYPE_MOVIE)

    private var testedContentPos = 0
    private val fakeData = FakeMovieData.getExpectedShortData()

    private val mOnItemClick : ItemClickSupport.OnItemClickListener = object : ItemClickSupport.OnItemClickListener {
        override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
            assertThat(testedContentPos, `is`(position))
            val adapter = recyclerView.adapter
            assertThat(adapter, notNullValue())
            val actualData = (adapter as RecyclerContentAdapter).mData[position]

            val expected = fakeData[testedContentPos]
            assertThat(expected.title, `is`(actualData.title))
            assertThat(expected.overview, `is`(actualData.overview))
            assertThat(expected.photoRes, `is`(actualData.photoRes))
            assertThat(expected.releaseDate, `is`(actualData.releaseDate))
        }

    }

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        activityRule.activity.setFragment(contentFragment)
        activityRule.activity.onItemClick = mOnItemClick
        IdlingRegistry.getInstance().register(activityRule.activity.countIdlingResource)
    }

    @Test
    fun testAll() {
        // check if recyclerview is displayed
        onView(withId(R.id.recycler_content_list)).check(matches(isDisplayed()))

        // check if the item's is same as expected
        onView(withId(R.id.recycler_content_list)).check(RecyclerViewItemAssertion(fakeData.size))

        // perform a swipe Up to check any ANR's related Memory or other
        onView(withId(R.id.recycler_content_list)).perform(swipeUp()).perform(swipeUp())

        // perform a swipe Down to check any ANR's related Memory or other
        onView(withId(R.id.recycler_content_list)).perform(swipeDown()).perform(swipeDown())

        // check the first element of item are same in data or not
        onView(withId(R.id.recycler_content_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerContentAdapter.ContentAdapterVH>(0, click()))


        // check the last element of item are same in data or not
        testedContentPos = fakeData.size - 1
        onView(withId(R.id.recycler_content_list)).perform(RecyclerViewActions.scrollToPosition<RecyclerContentAdapter.ContentAdapterVH>(testedContentPos))
        onView(withId(R.id.recycler_content_list)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerContentAdapter.ContentAdapterVH>(fakeData.size - 1, click()))
    }

    @After
    fun clear() {
        IdlingRegistry.getInstance().unregister(activityRule.activity.countIdlingResource)
    }
}