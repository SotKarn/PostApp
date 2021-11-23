package com.example.posts.views


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.posts.R
import com.example.posts.adapter.MyAdapter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class PostsFragmentTest{


    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
            scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun checkIfRecyclerViewExistsAndVisible() {
        onView(withId(R.id.mRecyclerView)).check(matches(isDisplayed())
        )
    }

    @Test
    fun check_fab_exists() {
        onView(withId(R.id.fab)).check(matches(isDisplayed()))
    }

    @Test
    fun check_fab_click_navigates_to_createPostFragment() {
        onView(withId(R.id.fab)).perform(click())

        onView(withId(R.id.create_post_input_id)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_recyclerView_item_is_clicked_it_navigates_to_detailsFragment()
    {

        onView(withId(R.id.mRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MyAdapter.ViewHolder>(1, click()))

        onView(withId(R.id.post_id)).check(matches(isDisplayed()))

    }

    @Test
    fun check_backPressed_from_details_returns_to_postFragment()
    {
        //Click an item from recyclerView to navigate to detailsFragment
        onView(withId(R.id.mRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition<MyAdapter.ViewHolder>(1, click()))

        //Back button pressed
        pressBack()

        //Verify it returns to postFragment
        onView(withId(R.id.mRecyclerView)).check(matches(isDisplayed()))
    }


}