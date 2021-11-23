package com.example.posts.views

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.posts.R
import org.junit.After
import org.junit.Before
import org.junit.Test


class CreatePostFragmentTest
{
    lateinit var scenario:FragmentScenario<CreatePostFragment>

    @Before
    fun setUp()
    {
        scenario = launchFragmentInContainer(null, R.style.Theme_Posts_NoActionBar)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun check_if_textFields_exists()
    {
        onView(withId(R.id.create_post_id)).check(matches(isDisplayed()))
        onView(withId(R.id.create_post_userId)).check(matches(isDisplayed()))
        onView(withId(R.id.create_post_title)).check(matches(isDisplayed()))
        onView(withId(R.id.create_post_body)).check(matches(isDisplayed()))
    }

    @Test
    fun check_if_textInputEditTexts_exists()
    {
        onView(withId(R.id.create_post_input_id)).check(matches(isDisplayed()))
        onView(withId(R.id.create_post_input_user_id)).check(matches(isDisplayed()))
        onView(withId(R.id.create_post_input_title)).check(matches(isDisplayed()))
        onView(withId(R.id.create_post_input_body)).check(matches(isDisplayed()))
    }

}