package id.apwdevs.app.motvcatalogue.activitytest

import android.content.Intent
import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import id.apwdevs.app.motvcatalogue.R
import id.apwdevs.app.motvcatalogue.activities.DetailActivity
import id.apwdevs.app.motvcatalogue.dataset.FakeMovieData
import id.apwdevs.app.motvcatalogue.model.DataModel
import id.apwdevs.app.motvcatalogue.plugin.AnotherAboutIntoHashMap
import id.apwdevs.app.motvcatalogue.plugin.RecyclerViewItemAssertion
import id.apwdevs.app.motvcatalogue.viewmodel.MainListTabViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailActivityTest {

    private val dataPosition = 0
    private val fakeData = FakeMovieData.getExpectedShortData()
    private val contentType = MainListTabViewModel.ContentDataType.TYPE_MOVIE

    @get:Rule
    val activityTestRule : ActivityTestRule<DetailActivity> = DetailActivityTestRule(fakeData[dataPosition], contentType, dataPosition)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(activityTestRule.activity.countIdlingResource)
    }

    @Test
    fun testAllCase() {
        val dataModel = fakeData[dataPosition]

        // is Expanded ?
        onView(withId(R.id.toolbar_layout)).check(matches(isDisplayed()))
        // check if the contentScrim is dataModel.title
        assertThat(dataModel.title.toString(), Is.`is`(activityTestRule.activity.toolbar_layout.title.toString()))

        // ensure that action fav is displayed
        onView(withId(R.id.action_favorite)).check(matches(isDisplayed()))

        // click button favorite, and ensure the snackbar is displayed
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withText(R.string.added_favorite)).check(matches(isDisplayed()))
        activityTestRule.activity.countIdlingResource.increment()

        // click button favorite, and ensure the snackbar is displayed
        onView(withId(R.id.action_favorite)).perform(click())
        onView(withText(R.string.deleted_favorite)).check(matches(isDisplayed()))
        activityTestRule.activity.countIdlingResource.increment()


        // collapse the toolbar
        onView(withId(R.id.detail_collapse)).perform(click())

        // toolbar is collapsed?
        assertThat(true, Is.`is`(activityTestRule.activity.isCollapsed))

        // check the recyclerview about, are its displayed?
        val aboutData = AnotherAboutIntoHashMap.intoHashMap(FakeMovieData.movieShortAbout[dataPosition], FakeMovieData.movieShortAboutKey.toList())
        onView(withId(R.id.short_about_detail)).check(matches(isDisplayed()))
        onView(withId(R.id.short_about_detail)).check(RecyclerViewItemAssertion(aboutData.size))

        // check all entries of about

        for ((key, value) in aboutData.entries){

            // check the key
            onView(withText(key)).check(matches(isDisplayed()))

            // check the value
            if (value.startsWith('$')){
                onView(withText(key)).check(matches(isDisplayed()))
            } else {
                value.split(",").forEach {valueSplitted ->
                    onView(withText(valueSplitted)).check(matches(isDisplayed()))
                }
            }
        }

        // ensure that releaseDate, and Overview are displayed within in the textView
        ////// get the text
        val textInTextView = activityTestRule.activity.detail_overview.text.toString()
        ///// ensure that is not empty
        assertThat(false, `is`(textInTextView.isEmpty()))

        // ensure the content of detail
        val containsOverview = textInTextView.contains(dataModel.overview.toString())
        val containsReleaseDate = textInTextView.contains(dataModel.releaseDate.toString())
        assertThat(true, `is`(containsReleaseDate))
        assertThat(true, `is`(containsOverview))

    }

    @After
    fun finished() {
        IdlingRegistry.getInstance().unregister(activityTestRule.activity.countIdlingResource)
    }
}

private class DetailActivityTestRule(
    private val rawDataContent: DataModel,
    private val contentType: MainListTabViewModel.ContentDataType,
    private val position: Int
) : ActivityTestRule<DetailActivity>(DetailActivity::class.java) {
    override fun getActivityIntent(): Intent = Intent(
            InstrumentationRegistry.getInstrumentation().targetContext,
            DetailActivity::class.java
        ).apply {
            putExtras(Bundle().also {
                it.putParcelable(DetailActivity.EXTRA_DATA, rawDataContent)
                it.putParcelable(DetailActivity.EXTRA_CONTENT_TYPES, contentType)
                it.putInt(DetailActivity.EXTRA_POSITION_DATA, position)
            })
        }
}