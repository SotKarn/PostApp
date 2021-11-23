package com.example.posts.views

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.posts.R
import org.junit.After
import org.junit.Test

class DetailsFragmentTest
{
    private val dummyData: Bundle = Bundle()
    private lateinit var scenario: FragmentScenario<DetailsFragment>

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun check_if_fields_are_correct_from_bundle() {
        val id = "1"
        val userId = "2"
        val title = "test title"
        val body = "test body"

        dummyData.putString("id", id)
        dummyData.putString("userId", userId)
        dummyData.putString("title", title)
        dummyData.putString("body", body)

        scenario = launchFragmentInContainer(dummyData,R.style.Theme_Posts_NoActionBar)

        onView(withId(R.id.post_id)).check(matches(withText(id)))
        onView(withId(R.id.post_user_id)).check(matches(withText(userId)))
        onView(withId(R.id.post_title)).check(matches(withText(title)))
        onView(withId(R.id.post_body)).check(matches(withText(body)))
    }

}