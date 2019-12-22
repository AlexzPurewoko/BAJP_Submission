package id.apwdevs.app.motvcatalogue.activitytest

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import com.google.android.material.appbar.CollapsingToolbarLayout
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.activities.MainActivity
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentAdapter
import id.apwdevs.app.motvcatalogue.adapter.RecyclerContentPagedAdapter
import id.apwdevs.app.motvcatalogue.dataset.FakeMovieData
import id.apwdevs.app.motvcatalogue.fragment.ParentFragment
import id.apwdevs.app.motvcatalogue.plugin.RecyclerViewItemAssertion
import kotlinx.android.synthetic.main.activity_main.*
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)


    private val dataPosition = 0
    private val fakeData = FakeMovieData.getExpectedShortData()

    @Before
    fun beforeRunning() {
        IdlingRegistry.getInstance().register(activityTestRule.activity.countIdlingResource)
    }

    @Test
    fun testAllCase(){

        activityTestRule.activity.apply {

            // ensure the title is same with app name
            onView(withText(R.string.app_name)).check(matches(withParent(withId(R.id.toolbar))))

            // ensure the app tablayout title is both are shown (Movies and tv Shows)
            val listStr = listOf(R.string.home_title, R.string.favorite_title)
            val listContentType = listOf(
                ParentFragment.Type.HOME,
                ParentFragment.Type.FAVORITE
            )
            for ((index, value) in listStr.withIndex()) {
                onView(withText(value)).check(matches(isDisplayed()))
                onView(withText(value)).perform(click())
                val parentFragment = mFragments[view_pager.currentItem]

                assertThat(listContentType[index], Is.`is`(parentFragment.currentType))

            }

            onView(withText(R.string.home_title)).perform(click())

            // perform click to a content in a recyclerview
            onView(allOf(withId(R.id.recycler_content_list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerContentAdapter.ContentAdapterVH>(dataPosition, click()))

            val dataTitle = fakeData[dataPosition].title.toString()
            onView(withId((R.id.toolbar_layout))).check { view, _ ->
                assertThat(true, Is.`is`(view is CollapsingToolbarLayout))
                val v = view as CollapsingToolbarLayout

                // ensure the content title is same as expected
                assertThat(dataTitle, Is.`is`(v.title))
            }

            // check and click favorites
            onView(withId(R.id.action_favorite)).check(matches(isDisplayed()))
            onView(withId(R.id.action_favorite)).perform(click())

            // ensure that navigateUp is displayed
            onView(withContentDescription(R.string.abc_action_bar_up_description)).check(matches(isDisplayed()))
            // click to back into previous
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())

            // ensure the navigateUp is working
            onView(withText(R.string.app_name)).check(matches(isDisplayed()))

            // move into favorite tabs
            onView(withText(R.string.favorite_title)).perform(click())

            // ensure that data is added into favorite
            onView(allOf(withId(R.id.recycler_content_list), isDisplayed())).check { view, noViewFoundException ->
                if(view is RecyclerView){
                    val adapter = view.adapter
                    if(adapter is RecyclerContentPagedAdapter){

                        val source = adapter.getData(0)
                        assertThat(dataTitle, `is`(source?.titleItem))
                        return@check
                    }
                }
                throw noViewFoundException
            }
            onView(allOf(withId(R.id.recycler_content_list), isDisplayed())).check(RecyclerViewItemAssertion(1))

            // click the item
            onView(allOf(withId(R.id.recycler_content_list), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerContentPagedAdapter.ContentAdapterVH>(0, click()))

            // click favorite and back into main activity
            onView(withId(R.id.action_favorite)).perform(click())
            onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click())

            // change into favorite tabs
            onView(withText(R.string.favorite_title)).perform(click())


            // checks thats the item is cleared away from databases
            onView(allOf(withId(R.id.recycler_content_list), isDisplayed())).check(RecyclerViewItemAssertion(0))
            onView(allOf(withId(R.id.fg_content_noresult), isDisplayed())).check { view, noViewFoundException ->
                if(view is TextView){
                    assertThat(getString(R.string.no_result), `is`(view.text))
                    return@check
                }
                throw noViewFoundException
            }
        }




    }

    @After
    fun finished() {
        IdlingRegistry.getInstance().unregister(activityTestRule.activity.countIdlingResource)
    }
}